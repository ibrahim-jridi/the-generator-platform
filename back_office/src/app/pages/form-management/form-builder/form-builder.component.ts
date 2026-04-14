import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {Formio, FormioComponent, FormioRefreshValue} from '@formio/angular';
import {PrismService} from 'src/app/shared/services/Prism.service';
import {pdf} from 'src/assets/formio/pdf';
import {wizard} from 'src/assets/formio/wizard';
import {form} from 'src/assets/formio/form';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SharedDataService} from 'src/app/shared/services/shared-data.service';
import {FormService} from 'src/app/shared/services/form.service';
import {Form} from 'src/app/shared/models/form.model';
import {Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {AppToastNotificationService} from '../../../shared/services/appToastNotification.service';
import {FormioService} from "../../../shared/services/formio.service";
import {ModalService} from "../../../shared/services/modal.service";

@Component({
  selector: 'app-form-builder',
  templateUrl: './form-builder.component.html',
  styleUrls: ['./form-builder.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FormBuilderComponent implements AfterViewInit, OnInit {
  @ViewChild('json', { static: true }) jsonElement?: ElementRef;
  @ViewChild('code', { static: true }) codeElement?: ElementRef;
  public form: any;
  public refreshForm: EventEmitter<FormioRefreshValue> = new EventEmitter();
  public formData: Form = new Form();
  public currentForm: any;
  public myForm: FormGroup;
  public options: any;
  public state = window.history.state;
  @ViewChild('formio') formioComponent: FormioComponent;
  protected formId: string;
  protected updated: boolean = false;

  constructor(
      public prism: PrismService,
      private readonly fb: FormBuilder,
      private readonly cdr: ChangeDetectorRef,
      private readonly sharedDataService: SharedDataService,
      private readonly formService: FormService,
      private readonly router: Router,
      private readonly toastrService: AppToastNotificationService,
      private readonly translatePipe: TranslatePipe,
      private readonly formioService: FormioService,
      private modalService: ModalService
  ) {
    this.form = form;
    this.options = this.formioService.getFormioOptions();
  }
  ngOnInit(): void {
    this.handleOnInitForm();
  }

  private handleOnInitForm(): void {
    this.myForm = this.fb.group({
      label: ['', Validators.required],
      description: ['', Validators.required],
      type: ['']
    });
    if (this.state.formId != null) {
      this.formId = this.state.formId;
      this.formData = this.state.formDTO;
      this.updated = true;
      this.myForm.patchValue({
        label: this.formData.label,
        description: this.formData.description
      });
      this.form = JSON.parse(this.formData.fields);
    } else if (this.state.formDTO) {
      this.formData = this.state.formDTO;
      this.myForm.patchValue({
        label: this.formData.label,
        description: this.formData.description
      });
      this.form = this.formData.fields;
    } else {
      this.form = this.formData.fields ? JSON.parse(this.formData.fields) : [];
    }
    this.currentForm = this.form;
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

  ngAfterViewInit() {
    this.prism.init();

    const token: string = sessionStorage.getItem('token')
    const BASE_URL: string = sessionStorage.getItem('BASE_URL');

    Formio.fetch = (url, options) => {
      options.headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`
      };
      return fetch(BASE_URL + url, options);
    };
    this.formioComponent.formioReady.then(() => {
          if (this.formioComponent && this.formioComponent.formio) {
            setTimeout(() => {
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
                switch (type) {
                  case 'modal':
                    this.modalService.showModal({
                      header: this.translatePipe.transform(title),
                      icon: 'Sticker.svg',
                      body: this.translatePipe.transform(body),
                      footer: '',
                      dialogClass: 'modal-dialog-centered',
                      hideHeader: false,
                      hideIcon: false,
                      hideFooter: false,
                      containerClick: false
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


            }, 300);
          }
        }
    )
  }

  onFormTypeChange(event) {
    const type = event.target.value;

    let newForm;
    switch (type) {
      case 'pdf':
        newForm = {...pdf};
        break;
      case 'form':
        newForm = {...form};
        break;
      case 'wizard':
        newForm = {...wizard};
        break;
      default:
        newForm = {...form};
    }

    this.form = null;
    this.cdr.detectChanges();

    this.form = newForm;
    this.refreshForm.emit({
      property: 'form',
      value: this.form,
    });

    this.cdr.detectChanges();
  }


  get f() {
    return this.myForm.controls;
  }
  submitForm() {
    this.formData.label = this.myForm.get('label').value;
    this.formData.description = this.myForm.get('description').value;

    if (!this.currentForm) {
      this.toastrService.onError(this.translatePipe.transform('form.INVALID_FORM'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    if (this.updated == true) {
      if (this.currentForm != null) {
        this.formData.fields = JSON.stringify(this.currentForm);
      }
      this.formData.createdDate = null;
      this.formService.updateForm(this.formId, this.formData).subscribe({
        next: () => {
          this.toastrService.onSuccess(this.translatePipe.transform('form.UPDATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
          this.router.navigate(['/pages/form-management']);
        },
        error: (error: any) => {
          this.toastrService.onError(this.translatePipe.transform('form.ADD_ERROR'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    } else {
      this.formData.fields = JSON.stringify(this.currentForm);
      this.formService.createForm(this.formData).subscribe({
        next: () => {
          this.toastrService.onSuccess(this.translatePipe.transform('form.ADD_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
          this.router.navigate(['/pages/form-management']);
        },
        error: (error: any) => {
          this.toastrService.onError(this.translatePipe.transform('form.ADD_ERROR'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    }
  }

  protected cancel(): void {
    this.router.navigate(['/pages/form-management']);
  }
}
