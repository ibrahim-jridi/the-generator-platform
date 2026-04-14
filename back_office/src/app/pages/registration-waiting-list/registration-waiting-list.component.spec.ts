import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationWaitingListComponent } from './registration-waiting-list.component';

describe('RegistrationWaitingListComponent', () => {
  let component: RegistrationWaitingListComponent;
  let fixture: ComponentFixture<RegistrationWaitingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegistrationWaitingListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RegistrationWaitingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
