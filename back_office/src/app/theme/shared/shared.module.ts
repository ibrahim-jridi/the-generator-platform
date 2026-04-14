import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AlertModule, BreadcrumbModule, CardModule, CustomButtonModule, CustomSelectModule, ModalModule } from './components';
import { ClickOutsideModule } from 'ng-click-outside';
import { ToastComponent } from './components/toast/toast.component';
import { ToastService } from './components/toast/toast.service';
import { NG_SCROLLBAR_OPTIONS, NgScrollbarModule } from 'ngx-scrollbar';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { GlobalService } from '../../shared/services/global.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { BackTopComponent } from './components/back-top/back-top.component';
import { CustomButtonComponent } from './components/custom-button/custom-button.component';
import { CustomDateAdapterComponent } from './components/custom-date-adapter/custom-date-adapter.component';
import { SearchSuggestionComponent } from './components/search-suggestion/search-suggestion.component';
import { SymbolPipe } from 'src/app/shared/pipes/symbol.pipe';
import { FileNamePipe } from 'src/app/shared/pipes/file-name.pipe';
import { SelectComponent } from './components/select/select.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormTaskComponent } from './components/form-task/form-task.component';
import { FormioModule } from '@formio/angular';
import { DateTimeFormatPipe } from '../../shared/pipes/date-time-format.pipe';
import {DateformatPipe} from "../../shared/pipes/date-transform.pipe";
import {FileUploadComponent} from "./components/file-upload/file-upload.component";
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';
import { CustomTableModule } from './components/custom-table/custom-table.module';

@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    AlertModule,
    BreadcrumbModule,
    CardModule,
    CustomButtonModule,
    ClickOutsideModule,
    NgScrollbarModule,
    ModalModule,
    TranslateModule,
    NgSelectModule,
    CustomSelectModule,
    FormioModule,
    NgbTooltip
  ],
  exports: [
    CommonModule,
    FormsModule,
    AlertModule,
    ReactiveFormsModule,
    ToastComponent,
    BreadcrumbModule,
    CardModule,
    CustomButtonModule,
    SpinnerComponent,
    ClickOutsideModule,
    ModalModule,
    CustomTableModule,
    BackTopComponent,
    TranslateModule,
    CustomButtonComponent,
    SearchSuggestionComponent,
    SymbolPipe,
    FileNamePipe,
    SelectComponent,
    CustomSelectModule,
    FormTaskComponent,
    DateformatPipe,
    DateTimeFormatPipe,
    FileUploadComponent
  ],
  declarations: [
    SpinnerComponent,
    ToastComponent,
    BackTopComponent,
    CustomDateAdapterComponent,
    SearchSuggestionComponent,
    SymbolPipe,
    FileNamePipe,
    SelectComponent,
    FormTaskComponent,
    DateformatPipe,
    DateTimeFormatPipe,
    FileUploadComponent
  ],
  providers: [
    {
      provide: NG_SCROLLBAR_OPTIONS,
      useValue: { scrollAuditTime: 20 }
    },
    ToastService,
    GlobalService
  ]
})
export class SharedModule {}
