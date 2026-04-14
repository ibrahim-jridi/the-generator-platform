import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import {AppToastNotificationService} from 'src/app/shared/services/appToastNotification.service';
import {
  ButtonActionModel
} from '../../../theme/shared/components/custom-table/model/button-action.model';
import {ConfigurationReportService} from "../../../shared/services/configuration-report.service";
import {RegexConstants} from 'src/app/shared/utils/regex-constants';
import {ConfigurationReportModel} from "../../../shared/models/configuration-report.model";
import {FileService} from "../../../shared/services/file.service";


@Component({
  selector: 'app-config-report-element',
  templateUrl: './config-report-element.component.html',
  styleUrls: ['./config-report-element.component.scss']
})
export class ConfigReportElementComponent implements OnInit {
  @ViewChild('fileInput') fileInput: ElementRef;
  public actionButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();
  public photoFile: File | null = null;
  public photoName: string = null;
  public allowedExtensions: string[] = ['jpg', 'jpeg', 'png', 'bmp', 'svg'];
  public allowedMimeTypes: string[] = ['image/jpeg', 'image/png', 'image/bmp', 'image/svg+xml'];
  public pageSize: number = 5;
  public totalSize: number;
  public messageLength: number = 0;
  public isConfigured: boolean = true;
  public isAdmin: boolean;
  public configurationReport: ConfigurationReportModel = new ConfigurationReportModel();

  protected isFirstConfig: boolean;
  protected reportForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private translateService: TranslateService,
    private router: Router,
    private route: ActivatedRoute,
    private configurationReportService: ConfigurationReportService,
    private fileService: FileService
  ) {}

  public ngOnInit(): void {
    this.getCurrentConfig();
    this.handleOnInitConfigReportForm();
  }

  public onFileSelected(file: File): void {
    this.photoFile = file;
    this.photoName = 'logo_' + this.reportForm.get('name').value + '.' + this.photoFile.name.split('.').pop().toLowerCase();
  }

  public handleOnInitConfigReportForm() {
    this.reportForm = this.formBuilder?.group(
      {
        name: ['', Validators.required],
        address: ['', [Validators.required, Validators.pattern(RegexConstants.COMPANY_ADDRESS)]],
        postalCode: ['', [Validators.required, Validators.pattern(RegexConstants.CODE_POSTAL)]],
        phone: ['', [Validators.required, Validators.pattern(RegexConstants.NUM_TEL)]],
        fax: ['', [Validators.required, Validators.pattern(RegexConstants.NUM_FAX)]],
        email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
        footer: [''],
        logo: ['', Validators.required]
      },
      { validator: [this.nameValidator] }
    );
  }

  protected nameValidator(formGroup: FormGroup): any {
    const companyName = formGroup.get('name')!.value;
    if (!RegexConstants.COMPANY_NAME.test(companyName) || companyName.length > 50) {
      return { companyName: true };
    }
    return null;
  }

  public getCurrentConfig(): void {
    this.configurationReportService.getConfigReport().subscribe({
      next: (data: any) => {
        if (data != undefined && data.length > 0) {
          this.isFirstConfig = false;
          this.configurationReport = data[0];
          this.reportForm.patchValue({
            name: this.configurationReport.name,
            address: this.configurationReport.address,
            postalCode: this.configurationReport.postalCode,
            phone: this.configurationReport.phone,
            fax: this.configurationReport.fax,
            email: this.configurationReport.email,
            footer: this.configurationReport.footer,
            lang: this.translateService.getDefaultLang(),
            logo: this.configurationReport.logo
          });
          this.photoName = this.configurationReport.logo;
          this.isConfigured = true;
          this.loadFile(this.photoName);
        } else {
          this.isFirstConfig = true;
          this.enableUpdate();
        }
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('configuration.report.typeMime'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public loadFile(imageName: string): void {
    this.fileService.getImageReportUrl(imageName).subscribe({
      next: (blob) => {
        const file = new File([blob], imageName, { type: blob.type });
        this.photoFile = file;
      },
      error: (err) => {
        console.error('Failed to load image:', err);
      }
    });
  }

  public updateCharacterCount(): void {
    this.messageLength = this.message.value?.length;
  }

  public onPhoneNumberInput(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9+\s\-\(\)]/g, '');
    if (inputElement.value.startsWith('+')) {
      inputElement.value = '+' + inputElement.value.substring(1).replace(/\+/g, '');
    }
  }

  private uploadFile(): void {
    this.fileService.uploadLogoReport(this.photoFile, this.photoName).subscribe({
      next: (data) => {},
      error: (error) => {}
    });
  }

  public fillDataConfigReport() {
    this.configurationReport.address = this.reportForm.get('address').value;
    this.configurationReport.name = this.reportForm.get('name').value;
    this.configurationReport.postalCode = this.reportForm.get('postalCode').value;
    this.configurationReport.phone = this.reportForm.get('phone').value;
    this.configurationReport.fax = this.reportForm.get('fax').value;
    this.configurationReport.email = this.reportForm.get('email').value;
    this.configurationReport.footer = this.reportForm.get('footer').value;
    this.configurationReport.logo = this.photoName;
    this.configurationReport.lang = this.translateService.getDefaultLang();
  }

  public addConfigurationReport(): void {
    if (!this.reportForm.valid) {
      return;
    }
    this.uploadFile();
    this.fillDataConfigReport();
    let addOrEdit;
    if (this.isConfigured) {
      addOrEdit = this.configurationReportService.updateConfigReport(this.configurationReport.id, this.configurationReport);
    } else {
      addOrEdit = this.configurationReportService.saveConfigReport(this.configurationReport);
    }

    addOrEdit.subscribe({
      next: (data) => {
        this.toastrService.onSuccess(
          this.translatePipe.transform('configuration.report.add_success'),
          this.translatePipe.transform('configuration.report.success')
        );
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('configuration.report.cannot_generate_report'),
          this.translatePipe.transform('configuration.report.ERROR')
        );
      }
    });
  }

  public isDisabled(): boolean {
    return !this.reportForm.valid;
  }

  public demoReport(): void {
    this.uploadFile();
    this.fillDataConfigReport();
    this.configurationReportService.demoReport(this.configurationReport).subscribe({
      next: (data) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(file);
        window.open(fileURL);
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('configuration.report.WARNING'),
          this.translatePipe.transform('configuration.report.WARNING')
        );
      }
    });
  }

  public enableUpdate() {
    this.isConfigured = false;
  }

  public cancel() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  public get message() {
    return this.reportForm?.get('footer')!;
  }

  public get email() {
    return this.reportForm?.get('email')!;
  }

  public get phone() {
    return this.reportForm?.get('phone')!;
  }

  public get fax() {
    return this.reportForm?.get('fax')!;
  }
}
