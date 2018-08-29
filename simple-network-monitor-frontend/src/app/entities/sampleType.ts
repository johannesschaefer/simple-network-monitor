import { Sensor } from "./sensor";

export interface SampleType {
    id: string;
    name: string;
    unit: string;
    sensor: Sensor;
}