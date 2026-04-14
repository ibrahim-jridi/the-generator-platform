import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateGrossisteComponent } from './create-grossiste.component';

const routes: Routes = [{ path: '', component: CreateGrossisteComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreateGrossisteRoutingModule { }
