import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreadcrumbComponent } from './breadcrumb.component';
import { RouterModule } from '@angular/router';
import { TranslateModule, TranslatePipe } from '@ngx-translate/core';

@NgModule({
    imports: [CommonModule, RouterModule, TranslateModule],
    declarations: [BreadcrumbComponent],
    exports: [BreadcrumbComponent],
    providers: [TranslatePipe]

})
export class BreadcrumbModule {}
