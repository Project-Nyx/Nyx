/*
 * Nyx - Server software for Minecraft: PE and more
 * Copyright © boredphoton 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectnyx.network.mcpe.raknet;

/**
 * Modified from:
 * <a href="https://github.com/OculusVR/RakNet/blob/master/Source/MessageIdentifiers.h">
 * RakNet: DefaultMessageIDTypes</a>
 */
public final class RakNetConsts {
    private RakNetConsts() {
    }

    public final static byte[] MAGIC = {
            (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00,
            (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd,
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
    };

    public final static byte RAKNET_CONNECTED_PING = (byte) 0x00;

    public final static byte RAKNET_UNCONNECTED_PING = (byte) 0x01;
    public final static byte RAKNET_UNCONNECTED_PING_OPEN_CONNECTIONS = (byte) 0x02;
    public final static byte RAKNET_CONNECTED_PONG = (byte) 0x03;
    public final static byte RAKNET_DETECT_LOST_CONNECTIONS = (byte) 0x04;
    public final static byte RAKNET_OPEN_CONNECTION_REQUEST_1 = (byte) 0x05;
    public final static byte RAKNET_OPEN_CONNECTION_REPLY_1 = (byte) 0x06;
    public final static byte RAKNET_OPEN_CONNECTION_REQUEST_2 = (byte) 0x07;
    public final static byte RAKNET_OPEN_CONNECTION_REPLY_2 = (byte) 0x08;
    public final static byte RAKNET_CONNECTION_REQUEST = (byte) 0x09;
    public final static byte RAKNET_REMOTE_SYSTEM_REQUIRES_PUBLIC_KEY = (byte) 0x0a;
    public final static byte RAKNET_OUR_SYSTEM_REQUIRES_SECURITY = (byte) 0x0b;
    public final static byte RAKNET_PUBLIC_KEY_MISMATCH = (byte) 0x0c;
    public final static byte RAKNET_OUT_OF_BAND_INTERNAL = (byte) 0x0d;
    public final static byte RAKNET_SND_RECEIPT_ACKED = (byte) 0x0e;
    public final static byte RAKNET_SND_RECEIPT_LOSS = (byte) 0x0f;
    public final static byte RAKNET_CONNECTION_REQUEST_ACCEPTED = (byte) 0x10;
    public final static byte RAKNET_CONNECTION_ATTEMPT_FAILED = (byte) 0x11;
    public final static byte RAKNET_ALREADY_CONNECTED = (byte) 0x12;
    public final static byte RAKNET_NEW_INCOMING_CONNECTION = (byte) 0x13;
    public final static byte RAKNET_NO_FREE_INCOMING_CONNECTIONS = (byte) 0x14;
    public final static byte RAKNET_DISCONNECTION_NOTIFICATION = (byte) 0x15;
    public final static byte RAKNET_CONNECTION_LOST = (byte) 0x16;
    public final static byte RAKNET_CONNECTION_BANNED = (byte) 0x17;
    public final static byte RAKNET_INVALID_PASSWORD = (byte) 0x18;
    public final static byte RAKNET_INCOMPATIBLE_PROTOCOL_VERSION = (byte) 0x19;
    public final static byte RAKNET_IP_RECENTLY_CONNECTED = (byte) 0x1a;
    public final static byte RAKNET_TIMESTAMP = (byte) 0x1b;
    public final static byte RAKNET_UNCONNECTED_PONG = (byte) 0x1c;
    public final static byte RAKNET_ADVERTISE_SYSTEM = (byte) 0x1d;
    public final static byte RAKNET_DOWNLOAD_PROGRESS = (byte) 0x1e;
    public final static byte RAKNET_REMOTE_DISCONNECTION_NOTIFICATION = (byte) 0x1f;
    public final static byte RAKNET_REMOTE_CONNECTION_LOST = (byte) 0x20;
    public final static byte RAKNET_REMOTE_NEW_INCOMING_CONNECTION = (byte) 0x21;
    public final static byte RAKNET_FILE_LIST_TRANSFER_HEADER = (byte) 0x22;
    public final static byte RAKNET_FILE_LIST_TRANSFER_FILE = (byte) 0x23;
    public final static byte RAKNET_FILE_LIST_REFERENCE_PUSH_ACK = (byte) 0x24;
    public final static byte RAKNET_DDT_DOWNLOAD_REQUEST = (byte) 0x25;
    public final static byte RAKNET_TRANSPORT_STRING = (byte) 0x26;
    public final static byte RAKNET_REPLICA_MANAGER_CONSTRUCTION = (byte) 0x27;
    public final static byte RAKNET_REPLICA_MANAGER_SCOPE_CHANGE = (byte) 0x28;
    public final static byte RAKNET_REPLICA_MANAGER_SERIALIZE = (byte) 0x29;
    public final static byte RAKNET_REPLICA_MANAGER_DOWNLOAD_STARTED = (byte) 0x2a;
    public final static byte RAKNET_REPLICA_MANAGER_DOWNLOAD_COMPLETE = (byte) 0x2b;
    public final static byte RAKNET_RAKVOICE_OPEN_CHANNEL_REQUEST = (byte) 0x2c;
    public final static byte RAKNET_RAKVOICE_OPEN_CHANNEL_REPLY = (byte) 0x2d;
    public final static byte RAKNET_RAKVOICE_CLOSE_CHANNEL = (byte) 0x2e;
    public final static byte RAKNET_RAKVOICE_DATA = (byte) 0x2f;
    public final static byte RAKNET_AUTOPATCHER_GET_CHANGELIST_SINCE_DATE = (byte) 0x30;
    public final static byte RAKNET_AUTOPATCHER_CREATION_LIST = (byte) 0x31;
    public final static byte RAKNET_AUTOPATCHER_DELETION_LIST = (byte) 0x32;
    public final static byte RAKNET_AUTOPATCHER_GET_PATCH = (byte) 0x33;
    public final static byte RAKNET_AUTOPATCHER_PATCH_LIST = (byte) 0x34;
    public final static byte RAKNET_AUTOPATCHER_REPOSITORY_FATAL_ERROR = (byte) 0x35;
    public final static byte RAKNET_AUTOPATCHER_FINISHED_INTERNAL = (byte) 0x36;
    public final static byte RAKNET_AUTOPATCHER_FINISHED = (byte) 0x37;
    public final static byte RAKNET_AUTOPATCHER_RESTART_APPLICATION = (byte) 0x38;
    public final static byte RAKNET_NAT_PUNCHTHROUGH_REQUEST = (byte) 0x39;
    public final static byte RAKNET_NAT_GROUP_PUNCHTHROUGH_REQUEST = (byte) 0x3a;
    public final static byte RAKNET_NAT_GROUP_PUNCHTHROUGH_REPLY = (byte) 0x3b;
    public final static byte RAKNET_NAT_CONNECT_AT_TIME = (byte) 0x3c;
    public final static byte RAKNET_NAT_GET_MOST_RECENT_PORT = (byte) 0x3d;
    public final static byte RAKNET_NAT_CLIENT_READY = (byte) 0x3e;
    public final static byte RAKNET_NAT_GROUP_PUNCHTHROUGH_FAILURE_NOTIFICATION = (byte) 0x3f;
    public final static byte RAKNET_NAT_TARGET_NOT_CONNECTED = (byte) 0x40;
    public final static byte RAKNET_NAT_TARGET_UNRESPONSIVE = (byte) 0x41;
    public final static byte RAKNET_NAT_CONNECTION_TO_TARGET_LOST = (byte) 0x42;
    public final static byte RAKNET_NAT_ALREADY_IN_PROGRESS = (byte) 0x43;
    public final static byte RAKNET_NAT_PUNCHTHROUGH_FAILED = (byte) 0x44;
    public final static byte RAKNET_NAT_PUNCHTHROUGH_SUCCEEDED = (byte) 0x45;
    public final static byte RAKNET_NAT_GROUP_PUNCH_FAILED = (byte) 0x46;
    public final static byte RAKNET_NAT_GROUP_PUNCH_SUCCEEDED = (byte) 0x47;
    public final static byte RAKNET_READY_EVENT_SET = (byte) 0x48;
    public final static byte RAKNET_READY_EVENT_UNSET = (byte) 0x49;
    public final static byte RAKNET_READY_EVENT_ALL_SET = (byte) 0x4a;
    public final static byte RAKNET_READY_EVENT_QUERY = (byte) 0x4b;
    public final static byte RAKNET_LOBBY_GENERAL = (byte) 0x4c;
    public final static byte RAKNET_RPC_REMOTE_ERROR = (byte) 0x4d;
    public final static byte RAKNET_RPC_PLUGIN = (byte) 0x4e;
    public final static byte RAKNET_FILE_LIST_REFERENCE_PUSH = (byte) 0x4f;
    public final static byte RAKNET_READY_EVENT_FORCE_ALL_SET = (byte) 0x50;
    public final static byte RAKNET_ROOMS_EXECUTE_FUNC = (byte) 0x51;
    public final static byte RAKNET_ROOMS_LOGON_STATUS = (byte) 0x52;
    public final static byte RAKNET_ROOMS_HANDLE_CHANGE = (byte) 0x53;
    public final static byte RAKNET_LOBBY2_SEND_MESSAGE = (byte) 0x54;
    public final static byte RAKNET_LOBBY2_SERVER_ERROR = (byte) 0x55;
    public final static byte RAKNET_FCM2_NEW_HOST = (byte) 0x56;
    public final static byte RAKNET_FCM2_REQUEST_FCMGUID = (byte) 0x57;
    public final static byte RAKNET_FCM2_RESPOND_CONNECTION_COUNT = (byte) 0x58;
    public final static byte RAKNET_FCM2_INFORM_FCMGUID = (byte) 0x59;
    public final static byte RAKNET_FCM2_UPDATE_MIN_TOTAL_CONNECTION_COUNT = (byte) 0x5a;
    public final static byte RAKNET_UDP_PROXY_GENERAL = (byte) 0x5b;
    public final static byte RAKNET_SQLite3_EXEC = (byte) 0x5c;
    public final static byte RAKNET_SQLite3_UNKNOWN_DB = (byte) 0x5d;
    public final static byte RAKNET_SQLLITE_LOGGER = (byte) 0x5e;
    public final static byte RAKNET_NAT_TYPE_DETECTION_REQUEST = (byte) 0x5f;
    public final static byte RAKNET_NAT_TYPE_DETECTION_RESULT = (byte) 0x60;
    public final static byte RAKNET_ROUTER_2_INTERNAL = (byte) 0x61;
    public final static byte RAKNET_ROUTER_2_FORWARDING_NO_PATH = (byte) 0x62;
    public final static byte RAKNET_ROUTER_2_FORWARDING_ESTABLISHED = (byte) 0x63;
    public final static byte RAKNET_ROUTER_2_REROUTED = (byte) 0x64;
    public final static byte RAKNET_TEAM_BALANCER_INTERNAL = (byte) 0x65;
    public final static byte RAKNET_TEAM_BALANCER_REQUESTED_TEAM_FULL = (byte) 0x66;
    public final static byte RAKNET_TEAM_BALANCER_REQUESTED_TEAM_LOCKED = (byte) 0x67;
    public final static byte RAKNET_TEAM_BALANCER_TEAM_REQUESTED_CANCELLED = (byte) 0x68;
    public final static byte RAKNET_TEAM_BALANCER_TEAM_ASSIGNED = (byte) 0x69;
    public final static byte RAKNET_LIGHTSPEED_INTEGRATION = (byte) 0x6a;
    public final static byte RAKNET_XBOX_LOBBY = (byte) 0x6b;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_INCOMING_CHALLENGE_SUCCESS = (byte) 0x6c;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_OUTGOING_CHALLENGE_SUCCESS = (byte) 0x6d;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_INCOMING_CHALLENGE_FAILURE = (byte) 0x6e;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_OUTGOING_CHALLENGE_FAILURE = (byte) 0x6f;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_OUTGOING_CHALLENGE_TIMEOUT = (byte) 0x70;
    public final static byte RAKNET_TWO_WAY_AUTHENTICATION_NEGOTIATION = (byte) 0x71;
    public final static byte RAKNET_CLOUD_POST_REQUEST = (byte) 0x72;
    public final static byte RAKNET_CLOUD_RELEASE_REQUEST = (byte) 0x73;
    public final static byte RAKNET_CLOUD_GET_REQUEST = (byte) 0x74;
    public final static byte RAKNET_CLOUD_GET_RESPONSE = (byte) 0x75;
    public final static byte RAKNET_CLOUD_UNSUBSCRIBE_REQUEST = (byte) 0x76;
    public final static byte RAKNET_CLOUD_SERVER_TO_SERVER_COMMAND = (byte) 0x77;
    public final static byte RAKNET_CLOUD_SUBSCRIPTION_NOTIFICATION = (byte) 0x78;
    public final static byte RAKNET_RESERVED_1 = (byte) 0x79;
    public final static byte RAKNET_RESERVED_2 = (byte) 0x7a;
    public final static byte RAKNET_RESERVED_3 = (byte) 0x7b;
    public final static byte RAKNET_RESERVED_4 = (byte) 0x7c;
    public final static byte RAKNET_RESERVED_5 = (byte) 0x7d;
    public final static byte RAKNET_RESERVED_6 = (byte) 0x7e;
    public final static byte RAKNET_RESERVED_7 = (byte) 0x7f;
    public final static byte RAKNET_RESERVED_8 = (byte) 0x80;
    public final static byte RAKNET_RESERVED_9 = (byte) 0x81;
    public final static byte RAKNET_USER_PACKET_ENUM = (byte) 0x82;
    public final static byte DATA_PACKET_MIN = (byte) 0x80;
    public final static byte DATA_PACKET_MAX = (byte) 0x8f;

    public final static byte NACK = (byte) 0xA0;
    public final static byte ACK = (byte) 0xC0;
    public final static byte DATA_PING = (byte) 0x00;
    public final static byte DATA_PONG = (byte) 0x03;
    public final static byte DATA_CLIENT_CONENCT = (byte) 0x09;
    public final static byte DATA_SERVER_HANDSHAKE = (byte) 0x10;
    public final static byte DATA_CLIENT_HANDSHAKE = (byte) 0x13;
    public final static byte DATA_CLIENT_DISCONNECT = (byte) 0x15;
    public final static byte DATA_MCPE = (byte) 0xfe;
}
