import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {
  DeclarationInterestsManagementComponent
} from "./declaration-interests-management.component";


const routes: Routes = [
  {
    path: '',
    component: DeclarationInterestsManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DeclarationInterestsManagementRoutingModule {
}
