import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BsConfigurationComponent} from "./bs-configuration.component";
import {DetailCostServiceComponent} from "./config-cost-service/detail-cost-service/detail-cost-service.component";

const routes: Routes = [
  {
    path: '',
    component: BsConfigurationComponent,
    children: [
      {
        path: 'detail-cost-service/:id',
        component: DetailCostServiceComponent,
      },
    ],
  },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BsConfigurationRoutingModule {}
