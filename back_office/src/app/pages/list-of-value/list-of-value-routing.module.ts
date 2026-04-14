import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListOfValueComponent } from './list-of-value.component';
import { ListOfValueAddComponent } from './list-of-value-add/list-of-value-add.component';
import { ListOfValueViewComponent } from './list-of-value-view/list-of-value-view.component';
import { ListOfValueUpdateComponent } from './list-of-value-update/list-of-value-update.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'list-of-value',
    pathMatch: 'full'
  },
  {
    path: '',
    component: ListOfValueComponent
  },
  {
    path: 'list-of-value-add/add',
    component: ListOfValueAddComponent
  },
  {
    path: 'list-of-value-view/:id',
    component: ListOfValueViewComponent
  },
  {
    path: 'list-of-value-update/:id',
    component: ListOfValueUpdateComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ListOfValueRoutingModule { }
