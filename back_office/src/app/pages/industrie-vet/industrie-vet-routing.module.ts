import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndustrieVetComponent } from './industrie-vet.component';

const routes: Routes = [{ path: '', component: IndustrieVetComponent}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IndustrieVetRoutingModule { }
