import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnsubscribeWaitingListComponent } from './unsubscribe-waiting-list.component';

describe('UnsubscribeWaitingListComponent', () => {
  let component: UnsubscribeWaitingListComponent;
  let fixture: ComponentFixture<UnsubscribeWaitingListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UnsubscribeWaitingListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UnsubscribeWaitingListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
