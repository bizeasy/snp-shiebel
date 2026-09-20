import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';

import { DepartmentFormGroup, DepartmentFormService } from './department-form.service';

@Component({
  selector: 'sys-department-update',
  templateUrl: './department-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DepartmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  department: IDepartment | null = null;

  departmentsSharedCollection = signal<IDepartment[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  productStoresSharedCollection = signal<IProductStore[]>([]);

  protected departmentService = inject(DepartmentService);
  protected departmentFormService = inject(DepartmentFormService);
  protected partyService = inject(PartyService);
  protected productStoreService = inject(ProductStoreService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartmentFormGroup = this.departmentFormService.createDepartmentFormGroup();

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareProductStore = (o1: IProductStore | null, o2: IProductStore | null): boolean =>
    this.productStoreService.compareProductStore(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.department = department;
      if (department) {
        this.updateForm(department);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const department = this.departmentFormService.getDepartment(this.editForm);
    if (department.id === null) {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDepartment | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(department: IDepartment): void {
    this.department = department;
    this.departmentFormService.resetForm(this.editForm, department);

    this.departmentsSharedCollection.update(departments =>
      this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, department.parent),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, department.head));
    this.productStoresSharedCollection.update(productStores =>
      this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, department.productStore),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.department?.parent),
        ),
      )
      .subscribe((departments: IDepartment[]) => this.departmentsSharedCollection.set(departments));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.department?.head)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.productStoreService
      .query()
      .pipe(map((res: HttpResponse<IProductStore[]>) => res.body ?? []))
      .pipe(
        map((productStores: IProductStore[]) =>
          this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, this.department?.productStore),
        ),
      )
      .subscribe((productStores: IProductStore[]) => this.productStoresSharedCollection.set(productStores));
  }
}
