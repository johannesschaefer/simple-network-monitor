import { Page } from "./page";

export interface CommandHal {
    _embedded: CommandEmbedded;
    page : Page;
}

export interface CommandEmbedded {
    commands : Command[];
}
export interface Command {
    id: string;
    name: string;
    description: string;
    exec: string;
    icon: string;
}