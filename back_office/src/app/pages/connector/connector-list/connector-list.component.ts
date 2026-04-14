import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { TranslatePipe } from '@ngx-translate/core';
import { ConnectorService } from '../../../shared/services/connector.service';
import { Connector } from '../../../shared/models/connector.model';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { ConnectorResponse } from '../../../shared/models/connector-response.model';

@Component({
  selector: 'app-connector-list',
  templateUrl: './connector-list.component.html',
  styleUrl: './connector-list.component.scss'
})
export class ConnectorListComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  public colonnesName: Array<CustomTableColonneModel>;
  public actionButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();
  public paginationArgs: PaginationArgs;
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  protected connectors: Connector[];
  filterValues: FilterValuesModel[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private connectorService: ConnectorService,
    private toastrService: AppToastNotificationService
  ) {}

  ngOnInit(): void {
    this.pageSize = 5;
    this.initTable();
    this.initTableAction();
  }

  protected redirectToAddConnector() {
    this.router.navigate(['add'], { relativeTo: this.route });
  }

  private initTable() {
    this.loadConnecters();
    const col1 = new CustomTableColonneModel(this.translatePipe.transform('connector.colurl'), 'url', false);
    const col2 = new CustomTableColonneModel(this.translatePipe.transform('connector.colapiname'), 'apiName', false);
    const col3 = new CustomTableColonneModel(this.translatePipe.transform('connector.colapilabel'), 'apiLabel', false);
    const col4 = new CustomTableColonneModel(this.translatePipe.transform('connector.colrequestmethod'), 'requestMethod', false);
    const col5 = new CustomTableColonneModel(this.translatePipe.transform('connector.colauthmethod'), 'authenticationMethod', false);
    this.colonnesName = new Array<CustomTableColonneModel>(col1, col2, col3, col4, col5);
  }

  protected loadConnecters() {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.pageIndex, size: this.pageSize };
    this.connectorService.getConnectors(this.filterValues, this.paginationArgs).subscribe({
      next: (response: ConnectorResponse) => {
        const totalCountHeader = response?.headers.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.connectors = response?.body;
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform(''), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  lineClicked(line: any) {}

  buttonAction(event: any) {
    if (event.buttonActionId === 'view') {
      this.handleOnViewConnector(event.objectClicked.id, event.objectClicked.label);
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateConnector(event.objectClicked.id, event.objectClicked.label);
    }
    if (event.buttonActionId === 'delete') {
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', this.translatePipe.transform('button.view'), 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(
      new ButtonActionModel('edit', this.translatePipe.transform('button.edit'), 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit')
    );
    this.actionButton.push(new ButtonActionModel('delete', this.translatePipe.transform('button.delete'), 'btn btn-icon', 'feather icon-trash'));
  }

  nextPage(event: any) {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.loadConnecters();
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadConnecters();
  }

  handleOnViewConnector(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        connectorId: id
      }
    };
    this.router.navigate(['view-connector', label], navigationExtras);
  }

  handleOnUpdateConnector(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        connectorId: id // Database id
      }
    };
    this.router.navigate(['update-connector', label], navigationExtras);
  }
}
