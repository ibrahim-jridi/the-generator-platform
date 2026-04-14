import {Component, OnInit} from "@angular/core";
import {
  CustomTableColonneModel
} from "../../theme/shared/components/custom-table/model/custom-table-colonne.model";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {
  ButtonOverlayModel
} from "../../theme/shared/components/custom-table/model/button-overlay.model";
import {
  ButtonActionModel
} from "../../theme/shared/components/custom-table/model/button-action.model";
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";

import {formatDate} from "@angular/common";
import {CalendarOptions} from "@fullcalendar/core";
import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import Validation from "../../shared/services/validation";


@Component({
  selector: 'app-demo',
  templateUrl: './demo.component.html',
  styleUrls: ['./demo.component.scss']
})
export class DemoComponent implements OnInit {
  public radioButtons: string;
  public checkBox: any;
  public tableData: Array<any>;
  public colonnesName: Array<CustomTableColonneModel>;
  public overlayButton: Array<ButtonOverlayModel> = new Array<ButtonOverlayModel>();
  public actionButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();

  public form: FormGroup;
  public submitted = false;

  dateObj = new Date();
  yearMonth = this.dateObj.getUTCFullYear() + '-' + (this.dateObj.getUTCMonth() + 1);
  calendarOptions: CalendarOptions = {
    plugins: [
      dayGridPlugin,
      interactionPlugin,
    ],
    initialView: 'dayGridMonth',
    events: [
      {
        title: 'All Day Event',
        start: formatDate(this.yearMonth + '-01', 'yyyy-MM-dd', 'en-US'),
        borderColor: '#04a9f5',
        backgroundColor: '#04a9f5',
        textColor: '#fff'
      },
      {
        title: 'Long Event',
        start: formatDate(this.yearMonth + '-07', 'yyyy-MM-dd', 'en-US'),
        end: formatDate(this.yearMonth + '-10', 'yyyy-MM-dd', 'en-US'),
        borderColor: '#f44236',
        backgroundColor: '#f44236',
        textColor: '#fff'
      },
      {
        id: '999',
        title: 'Repeating Event',
        start: formatDate(this.yearMonth + '-09 09:00:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        borderColor: '#f4c22b',
        backgroundColor: '#f4c22b',
        textColor: '#fff'
      },
      {
        id: '1000',
        title: 'Repeating Event',
        start: formatDate(this.yearMonth + '-16 08:00:00 AM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        borderColor: '#3ebfea',
        backgroundColor: '#3ebfea',
        textColor: '#fff'
      },
      {
        title: 'Conference',
        start: formatDate(this.yearMonth + '-11', 'yyyy-MM-dd', 'en-US'),
        end: formatDate(this.yearMonth + '-13', 'yyyy-MM-dd', 'en-US'),
        borderColor: '#1de9b6',
        backgroundColor: '#1de9b6',
        textColor: '#fff'
      },
      {
        title: 'Meeting',
        start: formatDate(this.yearMonth + '-12 10:00:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        end: formatDate(this.yearMonth + '-12 12:30:00 AM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        textColor: '#fff'
      },
      {
        title: 'Lunch',
        start: formatDate(this.yearMonth + '-12 12:00:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        borderColor: '#f44236',
        backgroundColor: '#f44236',
        textColor: '#fff'
      },
      {
        title: 'Meeting',
        start: formatDate(this.yearMonth + '-12 02:30:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        textColor: '#fff'
      },
      {
        title: 'Happy Hour',
        start: formatDate(this.yearMonth + '-12 05:30:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        borderColor: '#a389d4',
        backgroundColor: '#a389d4',
        textColor: '#fff'
      },
      {
        title: 'Dinner',
        start: formatDate(this.yearMonth + '-12 08:00:00 PM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        textColor: '#fff'
      },
      {
        title: 'Birthday Party',
        start: formatDate(this.yearMonth + '-13 07:30:00 AM', 'yyyy-MM-dd hh:mm:ss', 'en-US'),
        textColor: '#fff'
      },
      {
        title: 'Click for Google',
        url: 'http://google.com/',
        start: formatDate(this.yearMonth + '-28', 'yyyy-MM-dd', 'en-US'),
        borderColor: '#a389d4',
        backgroundColor: '#a389d4',
        textColor: '#fff'
      }
    ]
  };

  constructor(private translatePipe: TranslatePipe,
              private translateService: TranslateService,
              private toastrService: AppToastNotificationService,
              private formBuilder: FormBuilder) {
    this.radioButtons = '1';
    this.checkBox = {
      left: true,
      center: false,
      right: false
    };
  }

  ngOnInit() {
    this.initTable();
    this.initTableOverlay();
    this.initTableAction();
    this.translateService.onLangChange.subscribe(event => {
      this.initTable();
      this.initTableOverlay();
      this.initTableAction();
    });
    this.showToaster();
    this.initForm();
  }

  private initTable() {
    this.initData();
    const col1 = new CustomTableColonneModel(this.translatePipe.transform('demo.table.COL_NOM'), 'lastName', false)
    const col2 = new CustomTableColonneModel(this.translatePipe.transform('demo.table.COL_PRENOM'), 'firstName', false)
    const col3 = new CustomTableColonneModel(this.translatePipe.transform('demo.table.COL_AGE'), 'age', false)
    const col4 = new CustomTableColonneModel(this.translatePipe.transform('demo.table.COL_ADR'), 'adr', false)
    this.colonnesName = new Array<CustomTableColonneModel>(col1, col2, col3, col4);
  }

  lineClicked(line: any) {
    console.log("line clicked is: ", line);
  }

  initTableOverlay(): void {
    this.overlayButton.push(new ButtonOverlayModel('add', this.translatePipe.transform('demo.button.ADD')
        , 'btn btn-icon btn-success', 'fa fa-lg fa-check-circle'));
    this.overlayButton.push(new ButtonOverlayModel('id1', 'Action', 'btn btn-icon btn-success text-primary',
        'fa fa-hand-point-right'));

    const b1 = new ButtonOverlayModel('modify', 'modify', 'btn btn-icon btn-success', 'feather icon-edit');
    const b2 = new ButtonOverlayModel('rejected', 'rejected', 'btn btn-icon btn-danger', 'fa fa-lg fa-times-circle');
    this.overlayButton.push(b1, b2);
  }

  initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('edit', this.translatePipe.transform('demo.button.EDIT'), 'btn btn-icon btn-info', 'feather icon-edit'));
  }

  overlayButtonAction(event: any) {
    if (event.buttonOverlayId === 'add') {
      console.log("1 overlay button clicked is: ", event.objectClicked);
    }
    console.log("2 overlay button clicked is: ", event.objectClicked);
  }

  buttonAction(event: any) {
    if (event.buttonActionId === 'edit') {
      console.log("1 action button clicked is: ", event.objectClicked);
    }
  }

  nextPage(event: any) {
    console.log("loading page: ", event);
    const pageToLoad: number = event.pageNumber - 1;
    if (pageToLoad == 0) {
      this.initData();
    } else if (pageToLoad == 1) {
      this.tableData = [
        {
          id: '11',
          lastName: 'nom11',
          firstName: 'prenom11',
          age: '27',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '12',
          lastName: 'nom12',
          firstName: 'prenom12',
          age: '28',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '13',
          lastName: 'nom13',
          firstName: 'prenom13',
          age: '25',
          adr: '12 rue de capitaine Nemo'
        }, {
          id: '14',
          lastName: 'nom14',
          firstName: 'prenom14',
          age: '26',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '15',
          lastName: 'nom15',
          firstName: 'prenom15',
          age: '27',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '16',
          lastName: 'nom16',
          firstName: 'prenom16',
          age: '28',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '17',
          lastName: 'nom17',
          firstName: 'prenom17',
          age: '26',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '18',
          lastName: 'nom18',
          firstName: 'prenom18',
          age: '27',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '19',
          lastName: 'nom19',
          firstName: 'prenom19',
          age: '28',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '20',
          lastName: 'nom20',
          firstName: 'prenom20',
          age: '28',
          adr: '12 rue de capitaine Nemo'
        }
      ];
    } else {
      this.tableData = [
        {
          id: '21',
          lastName: 'nom21',
          firstName: 'prenom21',
          age: '27',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '22',
          lastName: 'nom22',
          firstName: 'prenom22',
          age: '28',
          adr: '12 rue de capitaine Nemo'
        },
        {
          id: '23',
          lastName: 'nom23',
          firstName: 'prenom23',
          age: '25',
          adr: '12 rue de capitaine Nemo'
        }
      ];
    }
  }

  private initData() {
    this.tableData = [
      {
        id: '1',
        lastName: 'nom1',
        firstName: 'prenom1',
        age: '25',
        adr: '12 rue de capitaine Nemo'
      }, {
        id: '2',
        lastName: 'nom2',
        firstName: 'prenom2',
        age: '26',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '3',
        lastName: 'nom3',
        firstName: 'prenom3',
        age: '27',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '4',
        lastName: 'nom4',
        firstName: 'prenom4',
        age: '28',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '5',
        lastName: 'nom5',
        firstName: 'prenom5',
        age: '25',
        adr: '12 rue de capitaine Nemo'
      }, {
        id: '6',
        lastName: 'nom6',
        firstName: 'prenom6',
        age: '26',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '7',
        lastName: 'nom7',
        firstName: 'prenom7',
        age: '27',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '8',
        lastName: 'nom8',
        firstName: 'prenom8',
        age: '28',
        adr: '12 rue de capitaine Nemo'
      },
      {
        id: '9',
        lastName: 'nom9',
        firstName: 'prenom9',
        age: '25',
        adr: '12 rue de capitaine Nemo'
      }, {
        id: '10',
        lastName: 'nom10',
        firstName: 'prenom10',
        age: '26',
        adr: '12 rue de capitaine Nemo'
      }
    ];
  }

  private showToaster(): void {
    this.toastrService.onSuccess('Information', 'Hello World');
  }

  public viewToasterError(): void {
    // for more example see https://artemsky.github.io/ng-snotify/
    this.toastrService.onError('Erreur', 'Simple Error');
  }

  public viewToasterSuccess(): void {
    // for more example see https://artemsky.github.io/ng-snotify/
    this.toastrService.onSuccess('Succes', 'Simple message');
  }

  private initForm(): void {
    this.form = this.formBuilder.group(
        {
          fullname: ['', Validators.required],
          username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]],
          email: ['', [Validators.required, Validators.email]],
          password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(40)]],
          confirmPassword: ['', Validators.required],
          acceptTerms: [false, Validators.requiredTrue]
        },
        {
          validators: [Validation.match('password', 'confirmPassword')]
        }
    );
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    console.log(JSON.stringify(this.form.value, null, 2));
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }
}
