import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { Report} from '../../../shared/models/report.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { ReportService } from 'src/app/shared/services/report.service';
import { Template } from 'src/app/shared/models/template.model';

@Component({
  selector: 'app-report-add',
  templateUrl: './report-add.component.html',
  styleUrl: './report-add.component.scss'
})
export class ReportAddComponent implements OnInit{
  protected template = new Template();
  protected reportForm: any;
  public isLoading : boolean = false;
  public progressBarValue : number = 0;
  public fileList: File[];
  public bucketName: string;
  public validFileFormat: boolean = true;
  public selectedFileName: string | null = null;
  public photoFile: File | null = null;
  @ViewChild('fileInput') fileInput: ElementRef;
  public allowedExtensions: string[] = ['jrxml'];
  public allowedMimeTypes: string[] = [''];

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private toastrService: AppToastNotificationService,
    private reportService:ReportService
  ) {
    this.initReportForm();
  }

  public ngOnInit(): void {

  }

  private initReportForm(): void {
    this.reportForm = this.fb.group({
      name: [
        '',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      description: [
        '',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      file: [
        '',
        [Validators.required, this.fileExtensionValidator.bind(this)]]

    });

  }



  public get name() {
    return this.reportForm?.get('name')!;
  }

  public get description() {
    return this.reportForm?.get('description')!;
  }

  public get file() {
    return this.reportForm?.get('file')!;
  }

  protected fileExtensionValidator(control: AbstractControl): { [key: string]: any } | null {

    const file = control.value as string;
    const extension = file.split('.').pop()?.toLowerCase();
    return this.allowedExtensions.includes(extension) ? null : { mismatch: true };

  }
  private fillReportData(): void {
    this.template.name = this.reportForm.value.name;
    this.template.description = this.reportForm.value.description;
  }

  protected addReport(): void {
    if (this.reportForm.valid && this.photoFile) {
      this.fillReportData();
      this.isLoading = true;

      this.reportService.uploadJrxmlFile(this.template, this.photoFile).subscribe({
        next: (response) => {
          this.viewToasterSuccess();
          this.router.navigate(['../'], { relativeTo: this.route });
          this.isLoading = false;
        },
        error: (err) => {
          this.viewToasterError();
          this.isLoading = false;
        }
      });
    } else {
      this.viewToasterError();
    }
  }



  protected navigateToUsers(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  protected back(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  public viewToasterError(): void {
    this.toastrService.onError(this.translatePipe.transform('report.errors.ADD_ERROR'), this.translatePipe.transform('menu.ERROR'));
  }

  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('report.ADD_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
  }

  public handleDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  public handleDrop(event: DragEvent): void {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    this.fileList = Array.from(files);
    this.photoFile = files[0];
    this.processSelectedFile();
    setTimeout(() => {
      this.isLoading = false;
      },1400);
  }

  public clearSelectedFile(): void {
    this.photoFile = null;
    this.selectedFileName = null;
    this.reportForm.get('file').setValue(null);
    this.validFileFormat = true;
  }

  public openFileInput() {
    this.fileInput.nativeElement.click();
  }
  public onFileSelected(event: any): void {
    this.fileList = event.target.files;
    if (this.fileList.length > 0) {
      this.photoFile = this.fileList[0];
      this.processSelectedFile();
    }
    setTimeout(() => {
      this.isLoading = false;
    }, 1400);
  }

  private processSelectedFile() : void{
    this.selectedFileName = this.photoFile.name;
    const fileExtension = this.photoFile.name.split('.').pop().toLowerCase();
    if (!this.allowedExtensions.includes(fileExtension)) {
      this.validFileFormat = false;
      this.toastrService.onError(this.translatePipe.transform('file.EXTENSION'), this.translatePipe.transform('menu.ERROR'));
      return;
    }else if (!this.allowedMimeTypes.includes(this.photoFile.type)) {
      this.validFileFormat = false;
      this.toastrService.onError(this.translatePipe.transform('file.TYPE_MIME'), this.translatePipe.transform('menu.ERROR'));
      return;
    }else{
      this.isLoading = true;
      this.progressBarValue = 0;
      const interval = 10;
      const totalIterations = 100;

      const incrementProgress = () => {
          this.progressBarValue++;
          if (this.progressBarValue < totalIterations) {
              setTimeout(incrementProgress, interval);
          }
      };
      setTimeout(incrementProgress, interval);
      this.validFileFormat = true;
    }
  }
}
