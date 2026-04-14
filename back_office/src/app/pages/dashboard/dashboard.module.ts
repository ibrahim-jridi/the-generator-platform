import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DashboardRoutingModule} from './dashboard-routing.module';
import {DashboardComponent} from './dashboard.component';
import {SharedModule} from '../../theme/shared/shared.module';
import { InfoCardComponent } from './info-card/info-card.component';
import { ChartComponent } from './chart/chart.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { ServicePanelComponent } from './service-panel/service-panel.component';

@NgModule({
  declarations: [DashboardComponent, InfoCardComponent, 
    ChartComponent, ServicePanelComponent],
    imports: [CommonModule, DashboardRoutingModule, SharedModule, NgApexchartsModule]
})
export class DashboardModule {
}
