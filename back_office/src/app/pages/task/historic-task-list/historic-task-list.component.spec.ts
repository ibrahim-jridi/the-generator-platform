import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricTaskListComponent } from './historic-task-list.component';

describe('HistoricTaskListComponent', () => {
  let component: HistoricTaskListComponent;
  let fixture: ComponentFixture<HistoricTaskListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoricTaskListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoricTaskListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
