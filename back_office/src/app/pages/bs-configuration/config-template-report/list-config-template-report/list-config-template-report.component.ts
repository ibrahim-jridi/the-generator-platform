import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { UiModalComponent } from '../../../../theme/shared/components/modal/ui-modal/ui-modal.component';
import { ReportTemplate } from '../../../../shared/models/report-template';
import { CustomTableColonneModel } from '../../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ButtonActionModel } from '../../../../theme/shared/components/custom-table/model/button-action.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppToastNotificationService } from '../../../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../../shared/models/paginationArgs.model';
import { FileService } from '../../../../shared/services/file.service';
import { ReportTemplateService } from '../../../../shared/services/report-template.service';
import { HttpResponse } from '@angular/common/http';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-list-config-template-report',
  templateUrl: './list-config-template-report.component.html',
  styleUrls: ['./list-config-template-report.component.scss']
})
export class ListConfigTemplateReportComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  public paginationArgs: PaginationArgs;
  @ViewChild('deleteTemplateReportModal') protected deleteTemplateReportModal: UiModalComponent;
  public reportTemplate: ReportTemplate = new ReportTemplate();
  public colonnesName: Array<CustomTableColonneModel>;
  public actionButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();
  public reportForm: FormGroup;
  public reportTemplates: Array<ReportTemplate>;
  public allowedExtensions: string[] = ['jrxml'];
  public totalSize: number;
  public reportTemplateId: string;
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  public reportTemplateName: string;

  @Output() viewDetails = new EventEmitter<string>();

  constructor(
    private formBuilder: FormBuilder,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private fileService: FileService,
    private reportTemplateService: ReportTemplateService
  ) {}

  public ngOnInit(): void {
    this.pageSize = 5;
    this.loadAllReportTemplate();
    this.initTable();
    this.initTableAction();
    this.handleOnInitReportForm();
  }

  private loadAllReportTemplate(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.pageIndex, size: this.pageSize };
    this.reportTemplateService.getAllReport(this.paginationArgs).subscribe({
      next: (response: HttpResponse<any>) => {
        const totalCountHeader = response?.headers?.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        const reportPromises = response?.body?.map((item: any) => {
          return this.createDownloadLink(item.type).then((downloadLink) => {
            return {
              ...item,
              lastModifiedDate: formatDate(item.lastModifiedDate, 'dd/MM/yyyy HH:mm', 'en-US'),
              path: downloadLink
            };
          });
        });

        Promise.all(reportPromises)
          .then((reports) => {
            this.reportTemplates = reports;
          })
          .catch((error) => {
            this.toastrService.onError(this.translatePipe.transform('config.error-message'), this.translatePipe.transform('menu.error'));
          });
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('config.error-message'), this.translatePipe.transform('menu.error'));
      }
    });
  }

  private createDownloadLink(fileName: string): Promise<string> {
    return new Promise((resolve, reject) => {
      this.fileService.getTemplateReportUrl(fileName).subscribe(
        () => {
          resolve(null);
        },
        (error) => {
          const url = error.error.text;
          if (url) {
            const downloadLink = `<a href="${url}" download>` + this.translatePipe.transform('configuration.report.download') + `</a>`;
            resolve(downloadLink);
          } else {
            resolve(null);
          }
        }
      );
    });
  }

  private initTable() {
    const col1 = new CustomTableColonneModel(this.translatePipe.transform('configuration.report.createdDate'), 'lastModifiedDate', false);
    const col3 = new CustomTableColonneModel(this.translatePipe.transform('configuration.report.type'), 'type', false);
    const col2 = new CustomTableColonneModel(this.translatePipe.transform('configuration.report.path'), 'path', false, '', true);
    this.colonnesName = new Array<CustomTableColonneModel>(col1, col2, col3);
  }

  private initTableAction(): void {
    this.actionButton.push(
      new ButtonActionModel('edit', this.translatePipe.transform('button.EDIT'), 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit')
    );
    this.actionButton.push(new ButtonActionModel('delete', this.translatePipe.transform('button.DELETE'), 'btn btn-icon', 'feather icon-trash'));
  }

  public handleOnInitReportForm() {
    this.reportForm = this.formBuilder?.group({
      report: [null, Validators.required],
      file: [null, Validators.required]
    });
  }

  public isDisabled() {
    return this.reportForm.invalid;
  }

  public handleSavePanelMessage() {
    this.reportTemplate.type = this.reportForm.get('report').value;
    this.reportTemplate.file = this.reportForm.get('file').value;
    if (this.reportTemplate.type != null && this.reportTemplate.file != null) {
      this.fileService.uploadTemplateReport(this.reportTemplate.file, this.reportTemplate.type).subscribe({
        next: (data) => {
          this.resetForm();
          this.loadAllReportTemplate();
          this.toastrService.onSuccess(this.translatePipe.transform('configuration.report.dataSaved'), this.translatePipe.transform('menu.SUCCESS'));
        },
        error: (error) => {
          const errorDetail = JSON.parse(error.error).detail;
          if (errorDetail === 'INVALID_EXTENSION') {
            this.toastrService.onError(this.translatePipe.transform('configuration.report.extension'), this.translatePipe.transform('menu.ERROR'));
          } else if (errorDetail === 'TEMPLATE_EXIST') {
            this.toastrService.onError(
              this.translatePipe.transform('configuration.report.template_exist'),
              this.translatePipe.transform('menu.ERROR')
            );
          } else {
            this.toastrService.onError(
              this.translatePipe.transform('configuration.report.dataSaveError'),
              this.translatePipe.transform('menu.ERROR')
            );
          }
        }
      });
    }
  }

  private resetForm(): void {
    this.reportForm.reset();
  }

  public nextPage(event: any) {
    this.pageIndex = event.pageNumber - 1;
    this.loadAllReportTemplate();
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadAllReportTemplate();
  }

  public buttonAction(event: any) {
    if (event.buttonActionId === 'edit') {
      this.handleOnEditTemplate(event.objectClicked.id);
    }
    if (event.buttonActionId === 'delete') {
      this.deleteTemplateReportModal.show();
      this.reportTemplateId = event.objectClicked.id;
      this.reportTemplateName = event.objectClicked.type;
    }
  }

  private handleOnEditTemplate(id: string) {
    this.viewDetails.emit(id);
  }

  protected handleOnDeleteReportTemplate(): void {
    this.fileService.deleteReport(this.reportTemplateId).subscribe({
      next: () => {
        this.toastrService.onSuccess(
          this.translatePipe.transform('configuration.report.delete.success-message'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        this.loadAllReportTemplate();
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('configuration.report.delete.error-message'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }
}
