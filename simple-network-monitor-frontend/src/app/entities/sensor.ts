import { Host } from "./host";
import { Page } from "./page";
import { SampleType } from "./sampleType";
import { Command } from "./command";
import { Sample } from "./sample";
import { Status } from "./status";

export interface SensorHal {
    _embedded: SensorEmbedded;
    page : Page;
}

export interface SensorEmbedded {
    sensors : Sensor[];
}

export interface Sensor {
    id: string;
    name: string;
    host: Host;
    active: boolean;
    interval: number;
    command: Command;
    samples: Sample[];
    properties: { [index: string]: string };
    secretProperties: { [index: string]: string };
    sampleTypes: SampleType[];
    status: Status;
}
