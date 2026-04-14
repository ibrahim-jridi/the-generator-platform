import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {UiModalComponent} from "../../../../theme/shared/components/modal/ui-modal/ui-modal.component";
import {CustomTableColonneModel} from "../../../../theme/shared/components/custom-table/model/custom-table-colonne.model";
import {ButtonActionModel} from "../../../../theme/shared/components/custom-table/model/button-action.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppToastNotificationService} from "../../../../shared/services/appToastNotification.service";
import {TranslatePipe} from "@ngx-translate/core";
import {ReportTemplate} from "../../../../shared/models/report-template";
import {ReportTemplateService} from "../../../../shared/services/report-template.service";
import {FileService} from "../../../../shared/services/file.service";

@Component({
  selector: 'app-edit-config-template-report',
  templateUrl: './edit-config-template-report.component.html',
  styleUrls: ['./edit-config-template-report.component.scss']
})
export class EditConfigTemplateReportComponent  implements OnInit{
  @ViewChild('deleteTemplateReportModal') protected deleteTemplateReportModal: UiModalComponent;
  public reportTemplate: ReportTemplate = new ReportTemplate();
  public colonnesName: Array<CustomTableColonneModel>;
  public actionButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();
  public reportForm: FormGroup;
  public allowedExtensions: string[] = ['jrxml'];
  public pageSize: number = 5;
  public totalSize: number;
  @Input() reportTemplateId: string;
  @Output() back = new EventEmitter<void>();

  constructor(private formBuilder: FormBuilder,
              private toastrService: AppToastNotificationService,
              private translatePipe: TranslatePipe,
              private reportTemplateService: ReportTemplateService,
              private fileService: FileService
  ) {
  }

  public ngOnInit(): void {
    this.handleOnInitReportForm();
    this.loadReportTemplate(this.reportTemplateId);
  }

  private loadReportTemplate(reportTemplateId : string): void {
    this.reportTemplateService.getReportById(reportTemplateId).subscribe({
      next: (data: any) => {
        this.reportTemplate = data;
        this.reportForm.patchValue({
          report: this.reportTemplate.type,
          file: this.reportTemplate.type+'.jrxml'
        });
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('configuration.report.error-message'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }
  public handleOnInitReportForm() {
    this.reportForm = this.formBuilder?.group({
      report: [null, Validators.required],
      file: [
        '',
      ],
    });
  }

 public backClicked() {
    this.reportForm.reset();
    this.back.emit();
  }

  public handleUpdatePanelMessage() {
      this.reportTemplate.file = this.reportForm.get("file").value;
    this.reportTemplate.type = this.reportForm.get("report").value;

    if (this.reportTemplate.type != null && this.reportTemplate.file != null) {
      this.fileService.updateTemplateReport(this.reportTemplate.file, this.reportTemplateId, this.reportTemplate.type).subscribe({
        next: (data) => {
          this.toastrService.onSuccess(this.translatePipe.transform('configuration.report.dataSaved'), this.translatePipe.transform('menu.SUCCESS'));
          this.backClicked();
        },
        error: (error) => {
          if (error.error?.detail === "INVALID_EXTENSION") {
            this.toastrService.onError(this.translatePipe.transform('configuration.report.extension'), this.translatePipe.transform('menu.ERROR'));
          } else {
            this.toastrService.onError(this.translatePipe.transform('configuration.report.dataSaveError'), this.translatePipe.transform('menu.EROOR'));
          }
        }
      })
    }
  }
}
