import { Page } from "./page";
import { Sensor, SensorEmbedded } from "./sensor";
import { Command } from "./command";
import { Status } from "./status";

export interface HostHal {
    _embedded: HostEmbedded;
    page : Page;
}

export interface HostEmbedded {
    hosts : Host[];
}

export interface Host {
    id: string;
    name: string;
    description: string;
    hostname: string;
    ipv4: string;
    ipv6: string;
    _embedded: SensorEmbedded;
    //sensors: Sensor[];
    commands: Command[];
    properties: { [index: string]: string };
    status: Status;
    ok: number;
    warn: number;
    critical: number;
    unknown: number;
}