import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ConfigurationGedRoutingModule} from './configuration-ged-routing.module';
import {CustomButtonModule, ModalModule} from "../../theme/shared/components";
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {ReactiveFormsModule} from "@angular/forms";
import {ConfigurationGedComponent} from "./configuration-ged.component";
import {UpdateConfigurationGedComponent} from "./update-configuration-ged/update-configuration-ged.component";


@NgModule({
  declarations: [
    ConfigurationGedComponent,
    UpdateConfigurationGedComponent
  ],
  imports: [
    CommonModule,
    ConfigurationGedRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule
  ]
})
export class ConfigurationGedModule {
}
