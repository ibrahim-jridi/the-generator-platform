import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessHistoryComponent } from './process-history.component';

describe('ProcessHistoryComponent', () => {
  let component: ProcessHistoryComponent;
  let fixture: ComponentFixture<ProcessHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessHistoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProcessHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
