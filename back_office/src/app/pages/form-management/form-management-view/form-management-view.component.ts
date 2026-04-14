import { HttpClient } from '@angular/common/http';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
  ViewChild
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Formio, FormioComponent, FormioRefreshValue } from '@formio/angular';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Form } from 'src/app/shared/models/form.model';
import { FormService } from 'src/app/shared/services/form.service';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { FormioService } from '../../../shared/services/formio.service';
import { ModalService } from '../../../shared/services/modal.service';

@Component({
  selector: 'app-form-management-view',
  templateUrl: './form-management-view.component.html',
  styleUrl: './form-management-view.component.scss'
})
export class FormManagementViewComponent implements OnInit, AfterViewInit {
  @ViewChild('formio') formioComponent: FormioComponent;
  public refreshForm: EventEmitter<FormioRefreshValue> = new EventEmitter();
  formData: Form = new Form();
  templateForm: any = {};
  myForm: FormGroup;
  public renderOptions: any = {};
  public currentLanguage: string;
  public isFormReady: boolean = true;
  options: any;
  public state = window.history.state;

  constructor(private readonly formService: FormService,
              private readonly fb: FormBuilder,
              private readonly route: ActivatedRoute,
              public translate: TranslateService,
              private readonly http: HttpClient,
              private readonly toastrService: AppToastNotificationService,
              private readonly translatePipe: TranslatePipe,
              private readonly formioService: FormioService,
              private modalService: ModalService,
              private readonly cdr: ChangeDetectorRef
  ) {
    const formId: string = this.state.formId;

    this.myForm = this.fb.group({
      label: [{ value: '', disabled: true }, Validators.required],
      description: [{ value: '', disabled: true }, Validators.required],
      id: formId
    });

    this.route.paramMap.subscribe((params) => {
      let id = formId;
      this.formService.getFormById(id).subscribe((data) => {
        this.formData = data;
        this.templateForm = JSON.parse(this.formData.fields);
        this.myForm.patchValue({
          label: this.formData.label,
          description: this.formData.description
        });

        this.refreshForm.emit({
          property: 'form',
          value: this.templateForm
        });
      });
    });
  }

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.changeLang(this.currentLanguage);

  }

  ngAfterViewInit() {
    const token: string = sessionStorage.getItem('token');
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
    );
  }

  private changeLang(lang: string): void {
    this.loadTranslations(lang);
    this.translate.onLangChange.subscribe((event) => {
      this.loadTranslations(event.lang);
    });
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
}
