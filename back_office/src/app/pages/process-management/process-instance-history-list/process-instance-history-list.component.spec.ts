import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessInstanceHistoryListComponent } from './process-instance-history-list.component';

describe('ProcessInstanceHistoryListComponent', () => {
  let component: ProcessInstanceHistoryListComponent;
  let fixture: ComponentFixture<ProcessInstanceHistoryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessInstanceHistoryListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProcessInstanceHistoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
