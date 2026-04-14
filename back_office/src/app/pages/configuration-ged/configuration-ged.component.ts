import { Component, OnInit } from '@angular/core';
import { CustomTableColonneModel } from '../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ButtonActionModel } from '../../theme/shared/components/custom-table/model/button-action.model';
import { SizeConfig } from '../../shared/models/sizeConfig.model';
import { SizeConfigService } from '../../shared/services/size-config.service';
import { TranslatePipe } from '@ngx-translate/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'app-configuration-ged',
  templateUrl: './configuration-ged.component.html',
  styleUrl: './configuration-ged.component.scss'
})
export class ConfigurationGedComponent implements OnInit {
  protected sizeConfig: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected totalElements: number;
  protected sizeConfigId: string;

  constructor(
    private router: Router,
    private translatePipe: TranslatePipe,
    private sizeConfigService: SizeConfigService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    this.loadSizeConfigs();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadSizeConfigs();
  }

  private loadSizeConfigs(): void {
    this.sizeConfigService.getSizeConfigs().subscribe((data: SizeConfig[]) => {
      this.sizeConfig = data.map((item, index) => ({
        ...item,
        index: this.page * this.pageSize + index + 1
      }));
      this.totalElements = data.length;
    });
  }

  protected handleOnUpdateConfigurationGed(id: number) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        sizeConfigId: id
      }
    };

    this.router.navigate(['update-configuration-ged', id], navigationExtras);
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateConfigurationGed(event.objectClicked.id);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('edit', 'tooltip.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('sizeConfig.ID', 'index', false));
    this.tableColumns.push(new CustomTableColonneModel('sizeConfig.EXTENSION', 'extension', false));
    this.tableColumns.push(new CustomTableColonneModel('sizeConfig.SIZE', 'maxSize', false));
  }
}
