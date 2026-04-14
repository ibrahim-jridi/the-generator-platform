import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {Formio, FormioComponent, FormioRefreshValue} from '@formio/angular';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import {CamundaService} from '../../../../shared/services/camunda.service';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../../../../shared/services/user.service';
import {User} from '../../../../shared/models/user.model';
import {
  AppToastNotificationService
} from '../../../../shared/services/appToastNotification.service';
import {Form} from '../../../../shared/models/form.model';
import {Task} from '../../../../shared/models/task.model';
import {Variable, VariableList} from '../../../../shared/models/variable.model';
import {FormioService} from '../../../../shared/services/formio.service';
import {ModalService} from '../../../../shared/services/modal.service';
import {FormService} from '../../../../shared/services/form.service';
import {Router} from '@angular/router';
import {formatDate} from '@angular/common';
import {CommonService} from '../../../../shared/services/common.service';
import {WaitingListService} from '../../../../shared/services/waiting-list.service';
import {first} from 'rxjs/operators';
import {LoaderService} from "../../../../shared/services/loader.service";


@Component({
  selector: 'app-form-task.',
  templateUrl: './form-task.component.html'
})
export class FormTaskComponent implements OnInit, AfterViewInit {
  @ViewChild('json', {static: true}) jsonElement?: ElementRef;
  @ViewChild('code', {static: true}) codeElement?: ElementRef;
  @ViewChild('formio') formioComponent: FormioComponent;
  public refreshForm: EventEmitter<FormioRefreshValue> = new EventEmitter();
  public currentForm: Form;
  @Input() cardTitle: string;
  @Input() isCollapsed: boolean;
  @Input() name: string;
  @Input() assignee: string;
  @Input() startTime: string;
  @Input() endTime: string;
  @Input() submission: any = {};
  @Input() disabled: boolean;
  @Input() form: Form = {};
  @Input() task: Task;
  @Input() taskId: string;
  @Input() formId: string;
  @Input() showRemoveButton = false;
  @Input() showExportButton = true;
  @Output() formSubmitted: EventEmitter<Form> = new EventEmitter();
  public renderOptions: any = {};
  public options: any = {};
  public currentLanguage: string;
  public isFormReady: boolean = true;
  private user: User;
  @Output() dataEvent = new EventEmitter<any>();
  private processVariable: VariableList;

  constructor(
      private readonly translatePipe: TranslatePipe,
      private readonly camundaService: CamundaService,
      public readonly translate: TranslateService,
      private readonly http: HttpClient,
      private readonly userService: UserService,
      private readonly toastrService: AppToastNotificationService,
      private readonly formioService: FormioService,
      private modalService: ModalService,
      private readonly formService: FormService,
      private readonly router: Router,
      private commonService: CommonService,
      private readonly cdr: ChangeDetectorRef,
      private waitingListService: WaitingListService,
      private loaderService: LoaderService
  ) {
  }

  public ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.changeLang(this.currentLanguage);
    this.options = this.formioService.getFormioOptions();

    this.submission = {
      data: {
        ...this.submission
      }
    };
    if (!this.disabled) {
      this.disableInputs(this.form.components);
    }
  }

  private changeLang(lang: string): void {
    this.loadTranslations(lang);
    this.translate.onLangChange.subscribe((event) => {
      this.loadTranslations(event.lang);
    });
  }

  public ngAfterViewInit(): void {
    const token: string = sessionStorage.getItem('token');
    const BASE_URL: string = sessionStorage.getItem('BASE_URL');

    if (this.task != null && this.task.variables != undefined && this.task.variables != null) {
      this.processVariable = this.task.variables;
      this.setData(this.form.components);
    }

    Formio.fetch = (url, options) => {
      options.headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`
      };
      return fetch(BASE_URL + url, options);
    };
    this.formioComponent.formioReady.then(() => {
          setTimeout(() => {
            if (this.formioComponent && this.formioComponent.formio) {
              this.formioComponent.formio.on('customButtonClick', (type, body, title) => {
                switch (type) {
                  case 'success':
                    this.toastrService.onSuccess(this.translatePipe.transform(body), this.translatePipe.transform(title));
                    break;

                  case 'error':
                    this.toastrService.onError(this.translatePipe.transform(body), this.translatePipe.transform(title));
                    break;

                  case 'info':
                    this.toastrService.onInfo(this.translatePipe.transform(body), this.translatePipe.transform(title));
                    break;

                  case 'warning':
                    this.toastrService.onWarning(this.translatePipe.transform(body), this.translatePipe.transform(title));
                    break;
                  default:
                    this.toastrService.onWarning(
                        this.translatePipe.transform(body || 'Unknown notification type'),
                        this.translatePipe.transform(title || 'Warning')
                    );
                    break;
                }
              });
              this.formioComponent.formio.on('customSubmit', (type, body, title) => {
                this.commonService.validateTask.subscribe(() => {
                  switch (type) {
                    case 'modal':
                      this.modalService.showModal({
                        header: this.translatePipe.transform(title),
                        icon: 'Sticker.svg',
                        body: body,
                        footer: '',
                        dialogClass: 'modal-dialog-centered',
                        hideHeader: false,
                        hideIcon: false,
                        hideFooter: false,
                        containerClick: false
                      });
                      break;
                    case 'modal-request-submission':
                      this.modalService.showModal({
                        header: this.translatePipe.transform(title),
                        icon: 'Sticker.svg',
                        body: this.generateCustomSubmitModalBody(body),
                        footer: '',
                        dialogClass: 'modal-dialog-centered',
                        hideHeader: false,
                        hideIcon: false,
                        hideFooter: false,
                        containerClick: false
                      });
                      break;
                    case 'modal-request-submission-waiting-list':
                      this.waitingListService.getUserRank().pipe(first()).subscribe(rank => {
                        const modalBody = this.generateCustomSubmitModalDorWaitingListBody(body, rank);

                        this.modalService.showModal({
                          header: this.translatePipe.transform(title),
                          icon: 'Sticker.svg',
                          body: modalBody,
                          footer: '',
                          dialogClass: 'modal-dialog-centered',
                          hideHeader: false,
                          hideIcon: false,
                          hideFooter: false,
                          containerClick: false
                        });
                      });
                      break;


                    default:
                      this.toastrService.onWarning(
                          this.translatePipe.transform(body || 'Unknown notification type'),
                          this.translatePipe.transform(title || 'Warning')
                      );
                      break;
                  }
                });
              });
              this.formioComponent.formio.on('saveDraft', (args) => {
                this.saveDraft();
              });
            }

          }, 300);
        }
    );
  }

  disableInputs(components) {
    components.forEach((component) => {
      if (component.components && Array.isArray(component.components)) {
        this.disableInputs(component.components);
      }

      if (component.type === 'columns') {
        component.columns.forEach((column) => {
          this.disableInputs(column.components);
        });
      }

      if (component.type === 'table') {
        component.rows.forEach((row) => {
          row.forEach((cell) => {
            this.disableInputs(cell.components);
          });
        });
      }
      if (
          [
            'textfield',
            'textarea',
            'customdatagrid',
            'select',
            'email',
            'datetime',
            'day',
            'button',
            'number',
            'radio',
            'selectboxes',
            'checkbox',
            'url',
            'file'
          ].includes(component.type)
      ) {
        component.disabled = true;
      }
    });
  }

  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
  }

  onChange(event) {
    this.currentForm = event.form;

    this.jsonElement.nativeElement.innerHTML = '';
    this.jsonElement.nativeElement.appendChild(document.createTextNode(JSON.stringify(event.form, null, 4)));
    this.refreshForm.emit({
      property: 'form',
      value: event.form
    });
  }

  public generatePDF(taskId: string): void {
    const doc = new jsPDF();
    const colorHeader = '#2980ba';
    const taskDetailsRows = [
      {field: 'Task Name', value: this.task.name},
      {field: 'Assignee', value: this.task.assignee},
      {field: 'Created Date', value: this.task.startTime},
      {field: 'End Date', value: this.task.endTime},
      {field: '', value: ''}
    ];

    const submissionDetailsRows = Object.entries(this.task.submission).flatMap(([key, value]) => {
      const formatValue = (val: any) => (typeof val === 'object' ? JSON.stringify(val) : val);

      if (Array.isArray(value)) {
        return value.flatMap((item) =>
            Object.entries(item).map(([subKey, subValue]) => ({
              field: `${key}.${subKey}`,
              value: formatValue(subValue)
            }))
        );
      }

      return [
        {
          field: key,
          value: formatValue(value)
        }
      ];
    });
    const margin = {top: 50, bottom: 10};
    autoTable(doc, {
      startY: margin.top,
      head: [['Field', 'Value']],
      body: taskDetailsRows.map((row) => [row.field, row.value]),
      headStyles: {fillColor: colorHeader},
      styles: {
        fillColor: [211, 211, 211],
        overflow: 'linebreak',
        cellWidth: 'auto'
      },

      columnStyles: {
        0: {cellWidth: 80},
        1: {cellWidth: 100}
      },
      margin: {top: margin.top, bottom: margin.bottom}
    });

    doc.setFontSize(12);
    doc.setTextColor(0, 0, 0);
    const submissionDetails = this.translatePipe.transform('task.SUBMISSION_DETAILS');
    const taskDetails = this.translatePipe.transform('task.TASK_DETAILS');
    doc.text(submissionDetails, 14, 115);
    doc.text(taskDetails, 14, 45);

    autoTable(doc, {
      startY: +120,
      head: [['Field', 'Value']],
      body: submissionDetailsRows.filter((row) => row.value !== '[{}]').map((row) => [row.field, row.value]),
      headStyles: {fillColor: colorHeader},
      styles: {
        fillColor: [211, 211, 211],
        overflow: 'linebreak',
        cellWidth: 'auto'
      },

      columnStyles: {
        0: {cellWidth: 80},
        1: {cellWidth: 100}
      },
      margin: {top: margin.top, bottom: margin.bottom}
    });
    doc.save('tasks.pdf');
  }

  public onSubmit(submission: any): void {
    this.formSubmitted.emit(submission.data);
  }

  private loadTranslations(language: string): void {
    this.http.get(`../../../assets/i18n/${language}.json`).subscribe(
        (translations: any) => {
          this.renderOptions = {
            language: language,
            i18n: {
              [language]: translations.FORMIO
            }
          };

          this.isFormReady = false;
          setTimeout(() => {
            this.isFormReady = true;
            this.cdr.detectChanges();
          });
        }
    );
  }

  private setData(components: any[]): void {
    components.forEach((component) => {
      this.handleComponent(component);
    });
    // Emit updated form data
    this.emitUpdatedSubmission();
    this.sendData();
  }

  private handleComponent(component: any) {
    if (component.key) {
      this.updateSubmissionWithMatchedVariable(component);
    }

    // Handle nested components based on type
    if (component.components && Array.isArray(component.components)) {
      this.setData(component.components);
    }

    if (component.type === 'columns') {
      this.handleColumns(component.columns);
    }

    if (component.type === 'table') {
      this.handleTableRows(component.rows);
    }

  }

  private updateSubmissionWithMatchedVariable(component: any) {
    const matchedVariable = this.processVariable.find((variable: Variable<any>) => variable.name === component.key);

    if (matchedVariable) {

      // Handle simple variable types
      if (this.isSimpleType(matchedVariable.type) && !!this.processVariable) {
        this.updateSubmission(component.key, matchedVariable.value);
      }
    }
  }

  private handleColumns(columns: any[]) {
    columns.forEach((column) => {
      this.setData(column.components);
    });
  }


  private handleTableRows(rows: any[]) {
    rows.forEach((row) => {
      row.forEach((cell) => {
        this.setData(cell.components);
      });
    });
  }

  private updateSubmission(key: string, value: any) {
    if (!!this.formioComponent && this.formioComponent?.submission) {
      this.formioComponent.submission = {
        data: {
          ...this.formioComponent.submission.data,
          [key]: value
        }
      };
      this.emitUpdatedSubmission();
    }
  }

  private isSimpleType(value: any): boolean {
    return ['string', 'number', 'boolean'].includes(typeof value.toLowerCase());
  }

  private emitUpdatedSubmission() {
    if (!!this.formioComponent && this.formioComponent?.submission) {
      this.refreshForm.emit({
        property: 'submission',
        value: this.formioComponent.submission
      });
    }
  }

  private sendData(): void {
    const formioData = this.form;
    this.dataEvent.emit(formioData);
  }

  public saveDraft(): void {

    if (!this.formioComponent || !this.formioComponent.submission) {
      this.toastrService.onError(
          this.translatePipe.transform('task.FAILED_TO_SAVE_DRAFT'),
          this.translatePipe.transform('task.FAILED_TO_SAVE_DRAFT')
      );
      return;
    }

    const draftDTO = {
      submission: JSON.stringify(this.formioComponent.submission.data),
      formId: this.form.formId,
      instanceTaskId: this.taskId
    };
    this.loaderService.show();
    this.formService.saveDraftData(draftDTO).subscribe({
      next: (response) => {
        this.loaderService.hide();
        this.toastrService.onSuccess(
            this.translatePipe.transform('task.SUCCESS_TO_SAVE_DRAFT'),
            this.translatePipe.transform('task.SUCCESS_TO_SAVE_DRAFT')
        );
        console.log('Draft saved successfully:', response);
        this.router.navigate(['pages/task-management/task-list']);
      },
      error: (err) => {
        this.loaderService.hide();
        console.error('Error saving draft:', err);
        this.toastrService.onError(
            this.translatePipe.transform('task.FAILED_TO_SAVE_DRAFT'),
            this.translatePipe.transform('task.FAILED_TO_SAVE_DRAFT')
        );
      }
    });
  }

  generateCustomSubmitModalBody(body: string): string {
    let now: Date = new Date();
    return this.translatePipe.transform(body) + '\n' +
        this.translatePipe.transform('task.REQUEST_NUMBER') + ' : ' + this.task.businessKey + '\n' +
        this.translatePipe.transform('task.SUBMISSION_DATE') + ' : ' + formatDate(now, 'dd/MM/yyyy', 'en-US') + '\n' +
        this.translatePipe.transform('task.SUBMISSION_TIME') + ' : ' + formatDate(now, 'HH:mm:ss', 'en-US');
  }

  generateCustomSubmitModalDorWaitingListBody(body: string, rank: number | null): string {
    return this.translatePipe.transform(body) + '\n' +
        this.translatePipe.transform('task.RANK') + ' : ' + (rank ?? 'N/A') + '\n';
  }

}
