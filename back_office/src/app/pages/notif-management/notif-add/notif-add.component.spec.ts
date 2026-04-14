import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotifAddComponent } from './notif-add.component';

describe('NotifAddComponent', () => {
  let component: NotifAddComponent;
  let fixture: ComponentFixture<NotifAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotifAddComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotifAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
