import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomButtonComponent } from './custom-button.component';
import {NgbDropdownModule, NgbTooltip} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    NgbDropdownModule,
    NgbTooltip,
    /*AnimatorModule*/
  ],
  declarations: [CustomButtonComponent],
  exports: [CustomButtonComponent],
    providers: [
    ]
})
export class CustomButtonModule {}
