import { Page } from "./page";

export interface SettingHal {
    _embedded: SettingEmbedded;
    page : Page;
}

export interface SettingEmbedded {
    settings : Setting[];
}
export interface Setting {
    name: string;
    displayName: string;
    description: string;
    value: string;
    type: string;
    unit: string;
    required: boolean;
}