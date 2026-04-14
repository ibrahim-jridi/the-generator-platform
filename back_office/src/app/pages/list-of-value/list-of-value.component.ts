import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../shared/models/paginationArgs.model';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ListOfValueService } from '../../shared/services/list-of-value.service';

@Component({
  selector: 'app-list-of-value',
  templateUrl: './list-of-value.component.html',
  styleUrl: './list-of-value.component.scss'
})
export class ListOfValueComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected listOfValues: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected userId: string;
  protected totalElements: number;
  private paginationArgs: PaginationArgs;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private listOfValuesService: ListOfValueService
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadListOfValues();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadListOfValues();
  }

  private loadListOfValues(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.listOfValuesService.getListOfValues().subscribe((data) => {
      this.listOfValues = data;
    });
  }

  public redirectToAddForm(): void {
    this.router.navigate(['/pages/list-of-value/list-of-value-add/add']);
  }

  handleOnViewForm(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        valueId: id
      }
    };
    this.router.navigate(['/pages/list-of-value/list-of-value-view/', label], navigationExtras);
  }

  handleOnUpdateForm(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        valueId: id
      }
    };
    this.router.navigate(['/pages/list-of-value/list-of-value-update/', label], navigationExtras);
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewForm(event.objectClicked.id, event.objectClicked.firstName + ' ' + event.objectClicked.lastName);
      //this.router.navigate(['pages/user-management/internal-user-management/view'])
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateForm(event.objectClicked.id, event.objectClicked.firstName + ' ' + event.objectClicked.lastName);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('listOfValues.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('listOfValues.name', 'label', false));
    this.tableColumns.push(new CustomTableColonneModel('listOfValues.DESCRIPTION', 'description', false));
  }
}
