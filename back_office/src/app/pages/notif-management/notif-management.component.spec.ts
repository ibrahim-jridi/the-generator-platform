import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotifManagementComponent } from './notif-management.component';

describe('NotifManagementComponent', () => {
  let component: NotifManagementComponent;
  let fixture: ComponentFixture<NotifManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotifManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotifManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
