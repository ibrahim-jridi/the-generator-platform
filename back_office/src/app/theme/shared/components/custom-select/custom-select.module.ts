// custom-select.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { CustomSelectComponent } from './custom-select.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [CustomSelectComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgSelectModule,
    TranslateModule,  // Import TranslateModule here
  ],
  exports: [CustomSelectComponent]
})
export class CustomSelectModule { }
