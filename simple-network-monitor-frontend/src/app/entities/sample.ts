import { SampleType } from "./sampleType";
import { Sensor } from "./sensor";
import { Status } from "./status";
import { Page } from "./page";

export interface SampleHal {
    _embedded: SampleEmbedded;
    page : Page;
}

export interface SampleEmbedded {
    samples : Sample[];
}
export interface Sample {
    id: string;
    type: SampleType;
    time: Date;
    value: number;
    warn: number;
    critical: number;
    min: number;
    max: number;
    unit: string;
    msg: string;
    status: Status;
    sensor: Sensor;
}
