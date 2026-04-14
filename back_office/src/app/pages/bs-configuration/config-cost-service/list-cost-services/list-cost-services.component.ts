import {Component, Input, OnInit} from '@angular/core';
import {ModalModule} from "../../../../theme/shared/components";
import {SharedModule} from "../../../../theme/shared/shared.module";
import {CustomTableModule} from "../../../../theme/shared/components/custom-table/custom-table.module";
import {TranslateModule, TranslatePipe, TranslateService} from "@ngx-translate/core";
import {PaginationArgs, PaginationSortArgs, PaginationSortOrderType} from "../../../../shared/models/paginationArgs.model";
import {CustomTableColonneModel} from "../../../../theme/shared/components/custom-table/model/custom-table-colonne.model";
import {ButtonActionModel} from "../../../../theme/shared/components/custom-table/model/button-action.model";
import {FilterValuesModel} from "../../../../shared/models/filter-values-model";
import {CostService} from "../../../../shared/models/cost-service";
import {BsConfigService} from "../../../../shared/services/bs-config.service";
import {CostServiceCriteria} from "../../../../shared/models/CostServiceCriteria";
import {AppToastNotificationService} from "../../../../shared/services/appToastNotification.service";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {BsConfigurationComponent} from "../../bs-configuration.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-list-cost-services',
  standalone: true,
    imports: [
        ModalModule,
        SharedModule,
        CustomTableModule,
        TranslateModule
    ],
  templateUrl: './list-cost-services.component.html',
  styleUrl: './list-cost-services.component.scss'
})
export class ListCostServicesComponent implements OnInit{
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  protected costServices: CostService[] = [];
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected totalElements: number;
  private paginationArgs: PaginationArgs;
  protected viewUsersOfGroup: boolean = false;
  public state = window.history.state;
  public filterValues: FilterValuesModel[];
  private criteria: CostServiceCriteria = new CostServiceCriteria();
  public costServiceId: string;
  public showDetailCost: boolean = false;
  public selectedCostServiceId: string | null = null;
  private subscription: Subscription;
  @Input() categoryType!: string;
  constructor(
    private costConfigService: BsConfigService,
    private translateService: TranslateService,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private router: Router,
    private route: ActivatedRoute,
    private bsConfig: BsConfigurationComponent
  ) {}
  public ngOnInit(): void {
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    if(this.categoryType === 'creationEntreprise'){
      this.loadCostServices();
    }else{
      this.costServices = null;
    }
    this.translateService.onLangChange.subscribe(() => {
      this.loadCostServices();
    });
    this.subscription = this.bsConfig.costServiceDeleted$.subscribe(() => {
      this.loadCostServices();
    });
  }

  loadCostServices(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };

    this.costConfigService.getAllCostServices(this.criteria, this.paginationArgs, this.categoryType).subscribe({
      next: (response: any) => {
        const totalCountHeader = response?.headers?.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.costServices = response?.body?.map((costService, index) => ({
          ...costService,
          index: this.pageIndex * this.pageSize + index + 1,
          serviceName: costService?.serviceName?.toString() ?? 'N/A',
          categorieService: costService?.categorieService?.toString() ?? 'N/A',
          isFixed: costService?.isFixed
            ? this.translatePipe.transform('configuration.cost.fixed') : this.translatePipe.transform('configuration.cost.variable'),
          prix_ttc: costService?.prix_ttc ? costService.prix_ttc.toFixed(2) : "0.00",
          createdDate: costService?.createdDate  ? new Date(costService.createdDate) : null,
          isRenewable: costService?.isRenewable === true
            ? this.translatePipe.transform('configuration.cost.renewable')
            : costService?.isRenewable === false
              ? this.translatePipe.transform('configuration.cost.not_renewable')
              : '',
        }));

        this.totalElements = this.costServices.length;
        console.log("Loaded costServices:", this.costServices);
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('configuration.cost.error.Error_loading_cost_services'),
          this.translatePipe.transform('menu.ERROR')
        );
        this.costServices = [];
      }
    });
  }

  public nextPage(event): void {
    this.pageIndex = event.pageNumber - 1;
    this.loadCostServices();
  }

  public onPageSizeChange(newPageSize: number): void {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadCostServices();
  }

  private initTableColumns(): void {
    this.tableColumns = [
      new CustomTableColonneModel('configuration.cost.created_date', 'createdDate', false),
      new CustomTableColonneModel('configuration.cost.service_name', 'serviceName', false),
      // new CustomTableColonneModel('configuration.cost.type_of_cost', 'isFixed', false),
      new CustomTableColonneModel('configuration.cost.amount_TTC', 'prix_ttc', false),
      // new CustomTableColonneModel('configuration.cost.cost_nature', 'isRenewable', false),
    ];
  }
  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
  }
  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      const navigationExtras: NavigationExtras = {
        state: {
          costServiceId: event.objectClicked.id,
        }
      }
      this.costServiceId = event.objectClicked.id
      this.showDetailCost = true;
      this.router.navigate(['detail-cost-service',   this.costServiceId], { relativeTo: this.route });
    }
  }
}
