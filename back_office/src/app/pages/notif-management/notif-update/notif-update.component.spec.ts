import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotifUpdateComponent } from './notif-update.component';

describe('NotifUpdateComponent', () => {
  let component: NotifUpdateComponent;
  let fixture: ComponentFixture<NotifUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotifUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotifUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
