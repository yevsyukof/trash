#include <iostream>
#include <sys/socket.h>
#include <cstring>
#include <unistd.h>
#include <fcntl.h>
#include <memory>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <ctime>
#include "AddressMap.h"
#include "AddressInterpreter.h"

#define multicast_port 54000
#define check_delay 5 // seconds

extern int errno;

void bindMulticastUdpSocket(int multicastUdpSocket, char *multicastAddress, bool IPv4_mode) {
    if (IPv4_mode) {
        std::shared_ptr<sockaddr_in> multicastIPv4SockAddr = std::make_shared<sockaddr_in>();
        bzero((char *) multicastIPv4SockAddr.get(), sizeof(sockaddr_in));

        multicastIPv4SockAddr->sin_family = AF_INET;
        multicastIPv4SockAddr->sin_port = htons(multicast_port);
        inet_pton(AF_INET, multicastAddress, &multicastIPv4SockAddr->sin_addr);
        if (bind(multicastUdpSocket, (sockaddr *)multicastIPv4SockAddr.get(),
                  sizeof(sockaddr_in)) == -1) {
            perror("BIND IPv4 ERROR \n");
            close(multicastUdpSocket);
            exit(22);
        }
    } else {
        std::shared_ptr<sockaddr_in6> multicastIPv6SockAddr = std::make_shared<sockaddr_in6>();
        bzero((char *) multicastIPv6SockAddr.get(), sizeof(sockaddr_in6));

        multicastIPv6SockAddr->sin6_port = htons(multicast_port);
        inet_pton(AF_INET6, multicastAddress, &multicastIPv6SockAddr->sin6_addr);
        if (bind(multicastUdpSocket, (sockaddr *)multicastIPv6SockAddr.get(),
                 sizeof(sockaddr_in6)) == -1) {
            perror("BIND IPv6 ERROR \n");
            close(multicastUdpSocket);
            exit(23);
        }
    }
}

void joinMulticastGroup(int multicastUdpSocket, char *multicastAddress, bool IPv4_mode) {
    if (IPv4_mode) {
        ip_mreq mreq = {};
        inet_aton(multicastAddress, &(mreq.imr_multiaddr));
        mreq.imr_interface.s_addr = htonl(INADDR_ANY);
        if (setsockopt(multicastUdpSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) == -1) {
            perror("JOIN IPv4 ERROR \n");
            close(multicastUdpSocket);
            exit(24);
        }
    } else {
        ipv6_mreq mreq = {};
        inet_pton(AF_INET6, multicastAddress, &(mreq.ipv6mr_multiaddr));
        mreq.ipv6mr_interface = htonl(INADDR_ANY);
        if (setsockopt(multicastUdpSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) == -1) {
            perror("JOIN IPv6 ERROR \n");
            close(multicastUdpSocket);
            exit(25);
        }
    }
}

std::shared_ptr<sockaddr> createEndpoint(char *multicastAddress, bool IPv4_mode) {
    if (IPv4_mode) {
        auto *ipv4Endpoint = (sockaddr_in *) calloc(1, sizeof(sockaddr_in));
        ipv4Endpoint->sin_family = AF_INET;
        ipv4Endpoint->sin_port = htons(multicast_port);
        inet_pton(AF_INET, multicastAddress, &ipv4Endpoint->sin_addr);
        return {(sockaddr *) ipv4Endpoint, free};
    } else {
        auto *ipv6Endpoint = (sockaddr_in6 *) calloc(1, sizeof(sockaddr_in6));
        ipv6Endpoint->sin6_family = AF_INET6;
        ipv6Endpoint->sin6_port = htons(multicast_port);
        inet_pton(AF_INET, multicastAddress, &ipv6Endpoint->sin6_addr);
        return {(sockaddr *) ipv6Endpoint, free};
    }
}

int main(int argc, char **argv) {
    char *multicastAddress = argv[1];
    bool IPv4_mode = strstr(multicastAddress, ":") == nullptr;

    if (IPv4_mode) {
        std::cout << "IPv4_MODE" << std::endl;
    } else {
        std::cout << "IPv6_MODE" << std::endl;
    }

    int multicastUdpSocket = socket(IPv4_mode ? AF_INET : AF_INET6, SOCK_DGRAM, IPPROTO_UDP);
    fcntl(multicastUdpSocket, F_SETFL, O_NONBLOCK); // делаем неблокирующим

    const int optval = 1;
    if (setsockopt(multicastUdpSocket, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval)) == -1) {
        perror("BLOCKLESS SOCKET ERROR");
        close(multicastUdpSocket);
        exit(19);
    } // разрешаем реюзать порт

    bindMulticastUdpSocket(multicastUdpSocket, multicastAddress, IPv4_mode);
    joinMulticastGroup(multicastUdpSocket, multicastAddress, IPv4_mode);

    std::shared_ptr<sockaddr> endpoint = createEndpoint(multicastAddress, IPv4_mode);

    pid_t pid = getpid();
    pid_t senderPid;
    AddressMap addressMap;


    timespec curTime{};
    timespec lastCheckTime{};

    AddressInterpreter addressInterpreter(IPv4_mode);

    while (true) {
        sendto(multicastUdpSocket, &pid, sizeof(pid_t), 0, endpoint.get(), sizeof(endpoint));

        sockaddr senderAddress = {};
        socklen_t len;
        int cnt = recvfrom(multicastUdpSocket, &senderPid, sizeof(senderPid), 0, &senderAddress, &len);

        clock_gettime(CLOCK_REALTIME, &curTime);
        if (cnt > 0) {
            addressMap.handleNewContact(addressInterpreter.convertAddress(senderAddress, senderPid), curTime);
        }

        if (addressMap.hasUpdate() || curTime.tv_sec - lastCheckTime.tv_sec > check_delay) {
            addressMap.check();
            if (addressMap.hasUpdate()) {
                addressMap.print();
            }
            lastCheckTime = curTime;
        }

        sleep(0.5);
    }
}
