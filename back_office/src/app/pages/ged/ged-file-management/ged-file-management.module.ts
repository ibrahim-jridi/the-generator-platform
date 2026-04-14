import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NgSelectModule } from '@ng-select/ng-select';
import { AllFileMangementComponent } from './all-file-mangement/all-file-mangement.component';
import { AddExtensionComponent } from './components/add-extension/add-extension.component';
import { DataTableComponent } from './components/data-table/data-table.component';
import { DialogFolderComponent } from './components/dialog-folder/dialog-folder.component';
import { DialogRenameComponent } from './components/dialog-rename/dialog-rename.component';
import { FileManagementComponent } from './components/file-management/file-management.component';
import { ListExtensionComponent } from './components/list-extension/list-extension.component';
import { GedFileManagementRoutingModule } from './ged-file-management-routing.module';
import { GedFileManagementComponent } from './ged-file-management.component';
import { MyFileMangementComponent } from './my-file-mangement/my-file-mangement.component';
import {TranslateModule} from "@ngx-translate/core";
import {MatFormField} from "@angular/material/form-field";
import {ReactiveFormsModule} from "@angular/forms";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatIcon} from "@angular/material/icon";
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {SharedModule} from "../../../theme/shared/shared.module";
import {MatPaginator} from "@angular/material/paginator";
import {MatInput} from "@angular/material/input";
import {
  MatNestedTreeNode,
  MatTree,
  MatTreeNode,
  MatTreeNodeDef,
  MatTreeNodeOutlet, MatTreeNodePadding,
  MatTreeNodeToggle
} from "@angular/material/tree";
import {MatButton, MatIconButton, MatMiniFabButton} from "@angular/material/button";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";


@NgModule({
  declarations: [
    FileManagementComponent,
    AllFileMangementComponent,
    MyFileMangementComponent,
    GedFileManagementComponent,
    DialogFolderComponent,
    DialogRenameComponent,
    ListExtensionComponent,
    DataTableComponent,
    AddExtensionComponent
  ],
    imports: [
        CommonModule,
        GedFileManagementRoutingModule,
        SharedModule,
        MatMenuModule,
        MatTooltipModule,
        MatTooltipModule,
        MatMenuModule,
        NgSelectModule,
        MatCardModule,
        MatTabsModule,
        TranslateModule,
        MatFormField,
        ReactiveFormsModule,
        MatTable,
        MatColumnDef,
        MatHeaderCell,
        MatCell,
        MatCellDef,
        MatHeaderCellDef,
        MatHeaderRow,
        MatRow,
        MatRowDef,
        MatHeaderRowDef,
        MatDialogTitle,
        MatIcon,
        MatDialogContent,
        MatCard,
        MatCardHeader,
        MatCardContent,
        MatPaginator,
        MatInput,
        MatTree,
        MatTreeNode,
        MatTreeNodeDef,
        MatIconButton,
        MatTreeNodeToggle,
        MatNestedTreeNode,
        MatGridList,
        MatGridTile,
        MatMiniFabButton,
        MatTreeNodeOutlet,
        MatTreeNodePadding,
        MatButton
    ],
  //entryComponents: [DialogRenameComponent, AddExtensionComponent]
})
export class GedFileManagementModule { }
