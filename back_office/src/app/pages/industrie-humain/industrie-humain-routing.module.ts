import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndustrieHumainComponent } from './industrie-humain.component';

const routes: Routes = [{ path: '', component: IndustrieHumainComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IndustrieHumainRoutingModule { }
