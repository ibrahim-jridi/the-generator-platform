import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotifViewComponent } from './notif-view.component';

describe('NotifViewComponent', () => {
  let component: NotifViewComponent;
  let fixture: ComponentFixture<NotifViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotifViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotifViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
