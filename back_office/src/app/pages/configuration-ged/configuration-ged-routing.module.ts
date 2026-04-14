import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ConfigurationGedComponent} from "./configuration-ged.component";
import {UpdateConfigurationGedComponent} from "./update-configuration-ged/update-configuration-ged.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full'
  },
  {
    path: '',
    component: ConfigurationGedComponent
  },
  {
    path: 'update-configuration-ged/:id',
    component: UpdateConfigurationGedComponent
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurationGedRoutingModule {
}
