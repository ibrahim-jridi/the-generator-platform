import {Injectable} from '@angular/core';
import {TranslatePipe} from '@ngx-translate/core';
import {Authorities} from '../../../../shared/enums/authorities.enum';
import {TokenUtilsService} from '../../../../shared/services/token-utils.service';

export interface NavigationItem {
  id: string;
  title: string;
  type: 'item' | 'collapse' | 'group';
  translate?: string;
  icon?: string;
  hidden?: boolean;
  url?: string;
  classes?: string;
  exactMatch?: boolean;
  external?: boolean;
  target?: boolean;
  breadcrumbs?: boolean;
  function?: any;
  badge?: {
    title?: string;
    type?: string;
  };
  authority?: Authorities | string; // Update authority type to include string
  children?: Navigation[];
}

export interface Navigation extends NavigationItem {
  children?: NavigationItem[];
}

const NavigationItems = [
  {
    id: 'navigation',
    title: '',
    type: 'group',
    children: [
      {
        id: 'dashboard',
        title: 'menu.DASHBOARD',
        type: 'item',
        icon: 'icon-Dashboard',
        url: '/pages/dashboard',
        authority: ['BS_USER']
      },
      {
        id: 'users',
        title: 'menu.USERS',
        type: 'item',
        icon: 'icon-User',
        url: '/pages/user-management/internal-user-management',
        authority: ['BS_USERSMANG'],
        children: [
          {
            id: 'new-user',
            title: 'menu.NEW_USER',
            type: 'item',
            icon: 'feather icon-monitor',
            hidden: true,
            url: '/pages/user-management/internal-user-management/add'
          },
          {
            id: 'view-user',
            title: 'menu.VIEW_USER',
            type: 'item',
            icon: 'feather icon-monitor',
            hidden: true,
            url: `/pages/user-management/internal-user-management/view-user/:id`
          },
          {
            id: 'update-user',
            title: 'menu.UPDATE_USER',
            type: 'item',
            icon: 'feather icon-monitor',
            hidden: true,
            url: `/pages/user-management/internal-user-management/update-user/:id`
          }
        ]
      },
      {
        id: 'role',
        title: 'menu.ROLE',
        type: 'item',
        icon: 'icon-Roles',
        url: '/pages/role-management',
        authority: ['BS_ROLES'],
        children: [
          {
            id: 'new-role',
            title: 'menu.NEW_ROLE',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: '/pages/role-management/add'
          },
          {
            id: 'view-role',
            title: 'menu.VIEW_ROLE',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/view-role/:id`
          },
          {
            id: 'update-role',
            title: 'menu.UPDATE_ROLE',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/update-role/:id`
          },
          {
            id: 'users-role',
            title: 'menu.LIST_USER_ROLE',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/list-users-role/:id`
            // //authority: [Authorities.BS_USER_GROUP, Authorities.BS_ADMIN]
          }
        ]
      },

      {
        id: 'group',
        title: 'menu.GROUPS',
        type: 'item',
        icon: 'icon-Groupe',
        url: '/pages/group-management',
        authority: ['BS_GROUPS'],
        children: [
          {
            id: 'new-group',
            title: 'menu.NEW_GROUP',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: '/pages/group-management/add'
          },
          {
            id: 'view-group',
            title: 'menu.VIEW_GROUP',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/view-group/:id`
          },
          {
            id: 'update-group',
            title: 'menu.UPDATE_GROUP',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/update-group/:id`
          },
          {
            id: 'users-group',
            title: 'menu.LIST_USER_GROUP',
            type: 'item',
            icon: 'users',
            hidden: true,
            url: `/pages/role-management/list-users-group/:id`
            // //authority: [Authorities.BS_USER_GROUP, Authorities.BS_ADMIN]
          }
        ]
      },

      {
        id: 'process',
        title: 'menu.PROCESS',
        type: 'collapse',
        icon: 'icon-process',
        authority: ['BS_PROCESS'],
        children: [
          {
            id: 'bpmn-modeler',
            title: 'menu.BPMN_MODELER',
            type: 'item',
            url: '/pages/process-management/process-management-bpmn',
            children: [
              {
                id: 'new-bpmn-modeler',
                title: 'menu.NEW_BPMN_MODELER',
                type: 'item',
                icon: 'feather icon-settings',
                url: '/pages/process-management/process-management-bpmn/bpmn-modeler/add'
              },
              {
                id: 'view-bpmn-modeler',
                title: 'menu.VIEW_BPMN_MODELER',
                type: 'item',
                icon: 'feather icon-settings',
                url: '/pages/process-management/view-bpmn-modeler/:id'
              },
              {
                id: 'view-history',
                title: 'menu.HISTORY_INSTANCE',
                type: 'item',
                icon: 'feather icon-settings',
                url: 'pages/process-management/view-bpmn-modeler/:id/view-history/:id'
              },
              {
                id: 'update-bpmn-modeler',
                title: 'menu.UPDATE_BPMN_MODELER',
                type: 'item',
                icon: 'feather icon-settings',
                url: '/pages/process-management/update-bpmn-modeler/:id'
              }
            ]
          },
          {
            id: 'ai-bpmn-chat',
            title: 'menu.AI_BPMN_GENERATOR',
            type: 'item',
            icon: 'icon-ai-bpmn',
            url: '/pages/process-management/ai-bpmn-chat'
          }
          // {
          //   id: 'dmn-modeler',
          //   title: 'menu.DMN_MODELER',
          //   type: 'item',
          //   url: '/pages/process-management/process-management-dmn',
          //   children: [
          //     {
          //       id: 'new-dmn-modeler',
          //       title: 'menu.NEW_DMN_MODELER',
          //       type: 'item',
          //       icon: 'feather icon-settings',
          //       url: '/pages/process-management/process-management-dmn/dmn-modeler/add'
          //     },
          //     {
          //       id: 'update-dmn-modeler',
          //       title: 'menu.UPDATE_DMN_MODELER',
          //       type: 'item',
          //       icon: 'feather icon-settings',
          //       url: '/pages/process-management/update-dmn-modeler/:id'
          //     }
          //   ]
          // }
        ]
      },
      {
        id: 'task-management',
        title: 'menu.TASK',
        type: 'collapse',
        icon: 'icon-task',
        authority: [Authorities.BS_TASKS],
        children: [
          {
            id: 'historic-task',
            title: 'menu.historic_task',
            type: 'item',
            //authority: [Authorities.BS_TASKS, Authorities.BS_ADMIN],
            url: '/pages/task-management/historic-task-list',
            children: [
              {
                id: 'view-historic-task',
                title: 'menu.TASK_VIEW',
                type: 'item',
                url: '/pages/task-management/historic-task-list/view-historic-task/:id'
              }
            ]
          },
          {
            id: 'task',
            title: 'menu.active_task',
            type: 'item',
            //authority: [Authorities.BS_TASKS, Authorities.BS_ADMIN],
            url: '/pages/task-management/task-list',
            children: [
              {
                id: 'validate-task',
                title: 'menu.TASK_VALIDATE',
                type: 'item',
                url: '/pages/task-management/task-list/validate-task/:id'
              }
            ]
          }
        ]
      },
      {
        id: 'form',
        title: 'menu.FORM',
        type: 'collapse',
        icon: 'icon-Forms',
        authority: ['BS_FORMS'],
        children: [
          {
            // id: 'listOfValue',
            // title: 'menu.LIST_OF_VALUES',
            // type: 'item',
            // url: '/pages/list-of-value',
            //authority: ['BS_VALUES', 'BS_ADMIN'],
            children: [
              // {
              //   id: 'new-listOfValue',
              //   title: 'menu.NEW_LIST_OF_VALUES',
              //   type: 'item',
              //   icon: 'dashboard',
              //   hidden: true,
              //   url: '/pages/list-of-value/list-of-value-add/add'
              // },
              // {
              //   id: 'list-of-value-view',
              //   title: 'menu.LIST_OF_VALUES_VIEW',
              //   type: 'item',
              //   icon: 'dashboard',
              //   hidden: true,
              //   url: '/pages/list-of-value/list-of-value-view/id'
              // }
            ]
          },
          {
            id: 'list-form',
            title: 'menu.LIST_FORM',
            type: 'item',
            url: '/pages/form-management',
            children: [
              {
                id: 'new-form',
                title: 'menu.NEW_FORM',
                type: 'item',
                icon: 'dashboard',
                hidden: true,
                url: '/pages/form-management/form-builder/add'
              },
              {
                id: 'view-form',
                title: 'menu.VIEW_FORM',
                type: 'item',
                icon: 'dashboard',
                hidden: true,
                url: '/pages/form-management/view-form/:id'
              },
              {
                id: 'update-form',
                title: 'menu.UPDATE_FORM',
                type: 'item',
                icon: 'dashboard',
                hidden: true,
                url: '/pages/form-management/update-form/:id'
              }
            ]
          }
        ]
      },
      {
        id: 'ged-management',
        title: 'GED',
        type: 'collapse',
        icon: 'icon-File',
        children: [
          {
            id: 'file-manager',
            title: 'menu.FILE_MANAGER',
            type: 'item',
            icon: 'feather icon-folder',
            url: '/pages/ged-management/file-manager'
          },
          {
            id: 'my-file',
            title: 'menu.MY_FILES',
            type: 'item',
            icon: 'feather icon-user',
            url: '/pages/ged-management/my-file'
          }
        ]
      },
      // {
      //   id: 'report',
      //   title: 'menu.REPORT',
      //   type: 'item',
      //   icon: 'icon-Rapport',
      //   url: '/pages/report-management',
      //   //authority: ['BS_REPORTS', 'BS_ADMIN'],
      //   children: [
      //     {
      //       id: 'update-report',
      //       title: 'menu.UPDATE_REPORT',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: `/pages/report-management/update-report/:id`
      //     },
      //     {
      //       id: 'new-report',
      //       title: 'menu.NEW_REPORT',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: `/pages/report-management/add`
      //     },
      //     {
      //       id: 'view-report',
      //       title: 'menu.VIEW_REPORT',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: `/pages/report-management/view-report/:id`
      //     }
      //   ]
      // },
      // {
      //   id: 'notification-management',
      //   title: 'menu.NOTIF',
      //   type: 'item',
      //   icon: 'icon-notif',
      //   url: '/pages/notif-management',
      //   //authority: ['BS_NOTIFICATIONS', 'BS_ADMIN'],
      //   children: [
      //     {
      //       id: 'notif-view',
      //       title: 'menu.NEW_NOTIF',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: '/pages/notif-management/notif-view/id'
      //     },
      //     {
      //       id: 'notif-add',
      //       title: 'menu.ADD_NOTIF',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: '/pages/notif-management/notif-add/id'
      //     },
      //     {
      //       id: 'notif-update',
      //       title: 'menu.UPDATE_NOTIF',
      //       type: 'item',
      //       icon: 'users',
      //       hidden: true,
      //       url: '/pages/notif-management/notif-update/id'
      //     }
      //
      //   ]
      // },
      // {
      //   id: 'configuration',
      //   title: 'menu.CONFIG',
      //   type: 'item',
      //   icon: 'icon-Config',
      //   url: '/pages/bs-configuration',
      //   //authority: ['BS_ADMIN']
      // },
      // {
      //   id: 'audit-management',
      //   title: 'menu.audit',
      //   type: 'collapse',
      //   icon: 'icon-audit',
      //   //authority: [Authorities.BS_ADMIN, Authorities.BS_AUDIT],
      //   children: [
      //     {
      //       id: 'activity-audit',
      //       title: 'menu.activity_audit',
      //       type: 'item',
      //       //authority: [Authorities.BS_ADMIN, Authorities.BS_AUDIT],
      //       url: '/pages/audit-management/activity-audit',
      //       children: [
      //         {
      //           id: 'view-activity-audit',
      //           title: 'menu.view_activity_audit',
      //           type: 'item',
      //           hidden: true,
      //           //authority: [Authorities.BS_ADMIN, Authorities.BS_AUDIT],
      //           url: '/pages/audit-management/activity-audit/view-activity-audit/:id'
      //         }
      //       ]
      //     },
      //     {
      //       id: 'error-audit',
      //       title: 'menu.error_audit',
      //       type: 'item',
      //       //authority: [Authorities.BS_ADMIN, Authorities.BS_AUDIT],
      //       url: '/pages/audit-management/error-audit',
      //       children: [
      //         {
      //           id: 'view-error-audit',
      //           title: 'menu.view_error_audit',
      //           type: 'item',
      //           hidden: true,
      //           //authority: [Authorities.BS_ADMIN, Authorities.BS_AUDIT],
      //           url: '/pages/audit-management/error-audit/view-error-audit/:id'
      //         }
      //       ]
      //     }
      //   ]
      // },
      // {
      //   id: 'ECTD',
      //   title: 'menu.ECTD',
      //   type: 'item',
      //   icon: ' icon-user',
      //   url: '/pages/ectd',
      //   //authority: [Authorities.BS_ADMIN, Authorities.BS_ECTD]
      // },
      // {
      //   id: 'waiting_List',
      //   title: 'menu.WAITING_LIST',
      //   type: 'collapse',
      //   icon: 'icon-Service',
      //   //authority: ['BS_WAITLIST_UNSUBSCRIBE','BS_WAITLIST_REGISTRATION','BS_WAIT_RENEW_REGION_CHANGE','BS_WAIT_RENEW_CATEGOR_CHANG'],
      //   children: [
      //     {
      //       id: 'registration-waiting-list',
      //       title: 'menu.REGISTRATION_WAITING_LIST',
      //       type: 'item',
      //       url: '/pages/registration-waiting-list',
      //       children: [
      //         {
      //           id: 'registration-waiting-list',
      //           title: 'menu.REGISTRATION_WAITING_LIST',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //           //authority: [Authorities.BS_WAITLIST_REGISTRATION],
      //           url:`/pages/registration-waiting-list`
      //         },
      //       ]
      //     },
      //     {
      //       id: 'unsubscribe-waiting-list',
      //       title: 'menu.UNSUBSCRIPTION_WAITING_LIST',
      //       type: 'item',
      //       url: '/pages/unsubscribe-waiting-list',
      //       children: [
      //         {
      //           id: 'unsubscribe-waiting-list',
      //           title: 'menu.UNSUBSCRIPTION_WAITING_LIST',
      //           type: 'item',
      //           url: '/pages/unsubscribe-waiting-list',
      //           //authority: [Authorities.BS_WAITLIST_UNSUBSCRIBE]
      //         },
      //       ]
      //     },
      //     {
      //       id: 'renewal-region-waiting-list',
      //       title: 'menu.RENEWAL-REGION-WAITING-LIST',
      //       type: 'item',
      //       url:`/pages/renewal-region-waiting-list`,
      //       children: [
      //           {
      //             id: 'renewal-region-waiting-list',
      //             title: 'menu.RENEWAL-REGION-WAITING-LIST',
      //             type: 'item',
      //             url: '/pages/renewal-region-waiting-list',
      //             //authority: [Authorities.BS_WAIT_RENEW_REGION_CHANGE]
      //           },
      //
      //           ]
      //          },
      //     {
      //       id: 'renewal-category-waiting-list',
      //       title: 'menu.RENEWAL-CATEGORY-WAITING-LIST',
      //       type: 'item',
      //       url: '/pages/renewal-category-waiting-list',
      //       children: [
      //         {
      //           id: 'renewal-category-waiting-list',
      //           title: 'menu.RENEWAL-CATEGORY-WAITING-LIST',
      //           type: 'item',
      //           url: '/pages/renewal-category-waiting-list',
      //           //authority: [Authorities.BS_WAIT_RENEW_CATEGOR_CHANG]
      //         },
      //       ]
      //     },
      //   ]
      // },
      // {
      //   id: 'pct_management',
      //   title: 'menu.PCT',
      //   type: 'collapse',
      //   icon: 'icon-Service',
      //   //authority: [Authorities.BS_CREATE_PCT,Authorities.BS_CREATE_AGENCE_PCT,Authorities.BS_CREATE_SUBSIDIARY_PCT],
      //   children: [
      //     {
      //       id: 'create-pct',
      //       title: 'menu.CREATE_PCT',
      //       type: 'item',
      //       url: '/pages/create-pct',
      //       children: [
      //         {
      //           id: 'create-pct',
      //           title: 'menu.CREATE_PCT',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //            //authority: [Authorities.BS_CREATE_PCT],
      //           url: `/pages/create-pct`
      //         },
      //       ]
      //     },
      //     {
      //       id: 'subsidiary-pct',
      //       title: 'menu.SUBSIDIARY_PCT',
      //       type: 'item',
      //       url: '/pages/subsidiary-pct',
      //       children: [
      //         {
      //           id: 'subsidiary-pct',
      //           title: 'menu.SUBSIDIARY_PCT',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //            //authority: [Authorities.BS_CREATE_SUBSIDIARY_PCT],
      //           url: `/pages/subsidiary-pct`
      //         },
      //       ]
      //     },
      //     {
      //       id: 'agency-pct',
      //       title: 'menu.AGENCY_PCT',
      //       type: 'item',
      //       url: '/pages/agency-pct',
      //       children: [
      //         {
      //           id: 'agency-pct',
      //           title: 'menu.AGENCY_PCT',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //           //authority: [Authorities.BS_CREATE_AGENCE_PCT],
      //           url:`/pages/agency-pct`
      //         },
      //         ]
      //     }
      //   ]
      // },
      // {
      //   id: 'process-instance-history',
      //   title: 'menu.TRACK_REQUEST',
      //   type: 'item',
      //   icon: 'icon-process',
      //   url: '/pages/process-management/process-instance-history',
      //   //authority: [Authorities.BS_TRACK_REQUESTS],
      // },
      // {
      //   id: 'gestion-industrie',
      //   title: 'menu.GESTION_INDUSTRIE',
      //   type: 'collapse',
      //   icon: 'icon-Service',
      //   children: [
      //     {
      //       id: 'industrie-humain',
      //       title: 'menu.INDUSTRIE_HUMAIN',
      //       type: 'item',
      //       url: '/pages/industrie-humain',
      //       children: [
      //         {
      //           id: 'industrie-humain',
      //           title: 'menu.INDUSTRIE_HUMAIN',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //           url:`/pages/industrie-humain`
      //         },
      //       ]
      //     },
      //     {
      //       id: 'industrie-vet',
      //       title: 'menu.INDUSTRIE_VET',
      //       type: 'item',
      //       url: '/pages/industrie-vet',
      //       children: [
      //         {
      //           id: 'industrie-vet',
      //           title: 'menu.INDUSTRIE_VET',
      //           type: 'item',
      //           url: '/pages/industrie-vet'
      //         },
      //       ]
      //     }
      //   ]
      // },
      // {
      //   id: 'gestion-grossiste',
      //   title: 'menu.GESTION_GROSSISTE',
      //   type: 'collapse',
      //   icon: 'icon-Service',
      //   //authority: [Authorities.BS_CREATE_WHOLESALER],
      //   children: [
      //     {
      //       id: 'create-grossiste',
      //       title: 'menu.CREATE_GROSSISTE',
      //       type: 'item',
      //       url: '/pages/create-grossiste',
      //       children: [
      //         {
      //           id: 'create-grossiste',
      //           title: 'menu.CREATE_GROSSISTE',
      //           type: 'item',
      //           icon: 'icon-Forms',
      //           url:`/pages/create-grossiste`
      //         },
      //       ]
      //     }
      //   ]
      // },
      {
        id: 'logout',
        title: 'menu.LOGOUT',
        type: 'item',
        icon: ' icon-deconect',
        url: '/auth'
        //authority: [Authorities.BS_ADMIN, Authorities.BS_USER]
      }
    ]
  }
];

@Injectable()
export class NavigationItem {
  constructor(private translatePipe: TranslatePipe, private tokenUtilsService: TokenUtilsService) {}

  private filterNavigationItems(items): NavigationItem[] {
    return items
      .filter((item) => {
        if (item.id === 'ECTD' && this.tokenUtilsService.isPM()) {
          return false;
        }

        if (item.authority) {
          if (item.authority instanceof Array) {
            return item.authority.some((authority) => this.tokenUtilsService.hasUserRole(authority));
          } else {
            return this.tokenUtilsService.hasUserRole(item.authority);
          }
        }
        return true; // Include items without authority restrictions
      })
      .map((item) => {
        if (item.children) {
          item.children = this.filterNavigationItems(item.children);
        }
        return item;
      });

  }

  private translateTitles(items: NavigationItem[]) {
    items.forEach((item) => {
      if (item.title) {
        item.title = this.translatePipe.transform(item.title);
      }
      if (item.children) {
        this.translateTitles(item.children);
      }
    });
  }

  public get() {
    return this.filterNavigationItems(NavigationItems);
  }
}
