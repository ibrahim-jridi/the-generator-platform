import {Component, OnInit, ViewChild} from '@angular/core';
import {DiagramChangedEvent, ImportEvent, NgDmnComponent} from 'libs/ng-bpmn/src';
import {CamundaService} from '../../../shared/services/camunda.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {AppToastNotificationService} from '../../../shared/services/appToastNotification.service';
import {forkJoin, from, Observable, of} from 'rxjs';
import {catchError, switchMap} from 'rxjs/operators';
import {FileContentService} from '../../../shared/services/fileContent.service';

@Component({
  selector: 'app-dmn-modeler',
  templateUrl: './dmn-modeler.component.html',
  styleUrls: ['./dmn-modeler.component.scss']
})
export class DmnModelerComponent implements OnInit {
  private processDmnId: string;
  @ViewChild('dmn', {static: false}) public dmnComponent: NgDmnComponent;
  public processDmnData: string;
  public dmnUrl: string = '/assets/diagram.dmn';
  public template: any;
  public state = window.history.state;
  public fileContent: string | null;

  constructor(
      private readonly camundaService: CamundaService,
      private readonly router: Router,
      private readonly route: ActivatedRoute,
      private readonly toastService: AppToastNotificationService,
      private readonly translatePipe: TranslatePipe,
      private readonly fileContentService: FileContentService
  ) {
  }

  public ngOnInit(): void {
    this.initialize();
  }

  private initialize(): void {
    this.processDmnId = this.state.processDmnId;
    if (this.processDmnId) {
      this.loadDmnById(this.processDmnId);
    }
    this.fileContent = this.fileContentService.getFileContent();
    if (this.fileContent) {
      const blob = new Blob([this.fileContent], {type: 'text/xml'});
      this.dmnUrl = URL.createObjectURL(blob);
      this.loadDmnModel(this.fileContent);
    }
  }

  public handleImported(event: ImportEvent): void {
    const {type} = event;

    if (type === 'success') {
      this.toastService.onSuccess(
          this.translatePipe.transform('process.DMN_UPLOADED_SUCCESSFULLY'),
          this.translatePipe.transform('process.DMN_UPLOADED_SUCCESSFULLY')
      );
    } else if (type === 'error') {
      this.toastService.onError(
          this.translatePipe.transform('process.DMN_UPLOAD_FAILED'),
          this.translatePipe.transform('process.DMN_UPLOADED_FAILED')
      );
    }
  }

  public onChanged($event: DiagramChangedEvent): void {
    // Add any diagram change handling logic if needed
  }

  private async getDmnAttribute(attribute: string, target: string = 'decision'): Promise<string> {
    if (!this.dmnComponent) {
      return 'Unknown Process';
    }

    try {
      const result = await this.dmnComponent.saveXML();
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(result, 'text/xml');
      const processElement = xmlDoc.getElementsByTagName(target)[0];
      return processElement?.getAttribute(attribute) || '';
    } catch (error) {
      return '';
    }
  }

  private getDmnXml(): Observable<string> {
    if (!this.dmnComponent) {
      return of('Unknown Dmn');
    }

    return from(this.dmnComponent.saveXML()).pipe(
        switchMap((result) => of(result || 'Unknown Dmn')),
        catchError(() => of('Unknown Dmn'))
    );
  }

  private getCurrentDate(): string {
    const date = new Date();
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
  }

  public async saveDmnData(): Promise<void> {
    const drdName = await this.getDmnAttribute('name', 'definitions');
    const name = await this.getDmnAttribute('name');
    const ttl = await this.getDmnAttribute('camunda:historyTimeToLive');

    if (!name || name === '') {
      this.toastService.onError(
        this.translatePipe.transform('process.DMN_NAME_REQUIRED'),
        this.translatePipe.transform('process.FAILED_TO_SAVE_DMN')
      );
      return;
    }
    if (!drdName || drdName === '') {
      this.toastService.onError(
        this.translatePipe.transform('process.DRD_NAME_REQUIRED'),
        this.translatePipe.transform('process.FAILED_TO_SAVE_DMN')
      );
      return;
    }

    if (!ttl || ttl === '') {
      this.toastService.onError(
          this.translatePipe.transform('process.HISTORY_TIME_TO_LIVE_REQUIRED'),
          this.translatePipe.transform('process.PROCESS_WITHOUT_HISTORY_TIME_TO_LIVE')
      );
      return;
    }

    forkJoin({
      name: this.getDmnAttribute("name"),
      xml: this.getDmnXml()
    })
    .pipe(
        switchMap(({name, xml}) => {
          const dmnData = {
            name,
            xml
          };
          return this.camundaService.deployDmnDefinition(dmnData);
        }),
        catchError(() => {
          this.toastService.onError(
              this.translatePipe.transform('process.FAILED_TO_SAVE_DMN'),
              this.translatePipe.transform('process.FAILED_TO_SAVE_DMN')
          );
          return of(null);
        })
    )
    .subscribe((response) => {
      if (response) {
        this.toastService.onSuccess(
            this.translatePipe.transform('process.SUCCESS_TO_SAVE_DMN'),
            this.translatePipe.transform('process.SUCCESS_TO_SAVE_DMN')
        );
        this.router.navigate(['/pages/process-management/process-management-dmn']);
      }
    });
  }

  private loadDmnById(processDmnId: string): void {
    this.camundaService.getDecisionDefinitionById(processDmnId).subscribe({
      next: (processDmnXml: string) => {
        this.processDmnData = processDmnXml;

        const blob = new Blob([processDmnXml], {type: 'text/xml'});
        this.dmnUrl = URL.createObjectURL(blob);

        this.loadDmnModel(processDmnXml);
      },
      error: () => {
        this.toastService.onError(
            this.translatePipe.transform('process.DMN_UPLOAD_FAILED'),
            this.translatePipe.transform('process.DMN_UPLOAD_FAILED')
        );
      }
    });
  }

  private loadDmnModel(xml: string): Observable<string> {
    this.dmnComponent.importDiagram(xml);
    this.getDmnXml();
    return from(this.dmnComponent.saveXML()).pipe(
        switchMap((result) => of(result || 'Unknown Dmn')),
        catchError(() => of('Unknown Dmn'))
    );
  }

  protected cancel(): void {
    this.router.navigate(['/pages/process-management/process-management-dmn']);
  }
}
