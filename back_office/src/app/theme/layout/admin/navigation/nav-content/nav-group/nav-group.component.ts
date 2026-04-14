import { Component, Input, NgZone, OnInit, OnDestroy } from '@angular/core';
import { NavigationItem } from '../../navigation';
import { Location } from '@angular/common';
import { BSConfig } from '../../../../../../app-config';
import { WaitingListService } from '../../../../../../shared/services/waiting-list.service';
import { TokenUtilsService } from '../../../../../../shared/services/token-utils.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-nav-group',
  templateUrl: './nav-group.component.html',
  styleUrls: ['./nav-group.component.scss']
})
export class NavGroupComponent implements OnInit, OnDestroy {
  @Input() item: NavigationItem;
  @Input() layout1: boolean = false;
  @Input() activeId: any;
  public bS_Config: any;
  private userStatusSubscription: Subscription;

  constructor(
    private zone: NgZone,
    private location: Location,
    private waitingListService: WaitingListService,
    private tokenUtilService: TokenUtilsService
  ) {
    this.bS_Config = BSConfig.config;
  }

  ngOnInit() {
    let current_url = this.location.path();
    if (this.location['_baseHref']) {
      current_url = this.location['_baseHref'] + this.location.path();
    }
    const link = "a.nav-link[ href='" + current_url + "' ]";
    const ele = document.querySelector(link);
    if (ele !== null && ele !== undefined) {
      const parent = ele.parentElement;
      const up_parent = parent.parentElement.parentElement;
      const last_parent = up_parent.parentElement;
      if (parent.classList.contains('pcoded-hasmenu')) {
        if (this.bS_Config['layout'] === 'vertical') {
          parent.classList.add('pcoded-trigger');
        }
        parent.classList.add('active');
      } else if (up_parent.classList.contains('pcoded-hasmenu')) {
        if (this.bS_Config['layout'] === 'vertical') {
          up_parent.classList.add('pcoded-trigger');
        }
        up_parent.classList.add('active');
      } else if (last_parent.classList.contains('pcoded-hasmenu')) {
        if (this.bS_Config['layout'] === 'vertical') {
          last_parent.classList.add('pcoded-trigger');
        }
        last_parent.classList.add('active');
      }
    }
    if (this.tokenUtilService.isUserExternal()) {
      this.subscribeToUserStatus();
    }
    this.industryShowToUser();
  }

  private subscribeToUserStatus(): void {
    this.userStatusSubscription = this.waitingListService.userUnsubscribed.subscribe((result: boolean) => {
      const targetItem = this.item.children?.find(child => child.id === 'waiting_List');

      if (targetItem) {
        const nestedTargetItem = targetItem?.children?.find(child => child.id === 'registration-waiting-list');
        const unsubscribeTargetItem = targetItem?.children?.find(child => child.id === 'unsubscribe-waiting-list');
        if(nestedTargetItem) {
          nestedTargetItem.hidden = !result;
        }
        if(unsubscribeTargetItem) {
          unsubscribeTargetItem.hidden = result;
        }
        const renewalTargetItem  = targetItem?.children?.find(child => child.id === 'renewal-region-waiting-list');
           if(renewalTargetItem) {
          renewalTargetItem.hidden = result;
        }
        const renewalCategoryTargetItem  = targetItem?.children?.find(child => child.id === 'renewal-category-waiting-list');
        if(renewalCategoryTargetItem) {
          renewalCategoryTargetItem.hidden = result;
        }
      }
    });

    this.waitingListService.refreshUserStatus();
  }

private industryShowToUser(): void {
  const userQualified = this.tokenUtilService.isPP();
  const targetItemIndustry = this.item.children?.find(child => child.id === 'gestion-industrie');
  if (targetItemIndustry) {
    targetItemIndustry.hidden = !userQualified;
  }
}
  ngOnDestroy() {
    if (this.userStatusSubscription) {
      this.userStatusSubscription.unsubscribe();
    }
  }
}
