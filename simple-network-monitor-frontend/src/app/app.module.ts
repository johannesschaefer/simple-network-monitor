import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { BlockUIModule } from 'ng-block-ui';
import { BlockUIHttpModule } from 'ng-block-ui/http';
import { NgSelectModule } from '@ng-select/ng-select';
import { CustomFormsModule } from 'ng2-validation'

import { AppComponent } from './app.component';

import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { CommandsComponent } from './pages/commands/commands.component';
import { HostDetailComponent } from './pages/host-detail/host-detail.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';

import { HostListComponent } from './components/host-list/host-list.component';
import { StatusComponent } from './components/status/status.component';
import { CommandStarterComponent } from './components/command-starter/command-starter.component';
import { HostOverviewComponent } from './components/host-overview/host-overview.component';
import { SensorListComponent } from './components/sensor-list/sensor-list.component';
import { SensorDetailsComponent } from './components/sensor-details/sensor-details.component';
import { CommandListComponent } from './components/command-list/command-list.component';
import { PropertiesPanelComponent } from './components/properties-panel/properties-panel.component';
import { SettingsListComponent } from './components/settings-list/settings-list.component';
import { SensorEditAddComponent } from './components/sensor-edit-add/sensor-edit-add.component';

import { KeysPipe } from './services/keys.pipe';
import { TextFilterPipe } from './services/text-filter.pipe';


const appRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'host/:id', component: HostDetailComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'commands', component: CommandsComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HostListComponent,
    StatusComponent,
    CommandStarterComponent,
    HostOverviewComponent,
    HostDetailComponent,
    PageNotFoundComponent,
    SensorListComponent,
    SensorDetailsComponent,
    SettingsComponent,
    CommandsComponent,
    CommandListComponent,
    PropertiesPanelComponent,
    KeysPipe,
    SettingsListComponent,
    SensorEditAddComponent,
    TextFilterPipe
  ],
  
  exports: [
  ],
  imports: [
    BlockUIModule.forRoot(),
    //BlockUIHttpModule.forRoot(),
    NgSelectModule,
    ModalModule.forRoot(),
    NgxChartsModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: false }
    ),
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    CustomFormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    ModalModule.forRoot(),
    TooltipModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
