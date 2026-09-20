import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { IDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';

import { DepartmentFormService } from './department-form.service';
import { DepartmentUpdate } from './department-update';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdate;
  let fixture: ComponentFixture<DepartmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let departmentFormService: DepartmentFormService;
  let departmentService: DepartmentService;
  let partyService: PartyService;
  let productStoreService: ProductStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(DepartmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentFormService = TestBed.inject(DepartmentFormService);
    departmentService = TestBed.inject(DepartmentService);
    partyService = TestBed.inject(PartyService);
    productStoreService = TestBed.inject(ProductStoreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Department query and add missing value', () => {
      const department: IDepartment = { id: 15970 };
      const parent: IDepartment = { id: 29518 };
      department.parent = parent;

      const departmentCollection: IDepartment[] = [{ id: 29518 }];
      vi.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [parent];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      vi.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.departmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const department: IDepartment = { id: 15970 };
      const head: IParty = { id: 24527 };
      department.head = head;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [head];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductStore query and add missing value', () => {
      const department: IDepartment = { id: 15970 };
      const productStore: IProductStore = { id: 30593 };
      department.productStore = productStore;

      const productStoreCollection: IProductStore[] = [{ id: 30593 }];
      vi.spyOn(productStoreService, 'query').mockReturnValue(of(new HttpResponse({ body: productStoreCollection })));
      const additionalProductStores = [productStore];
      const expectedCollection: IProductStore[] = [...additionalProductStores, ...productStoreCollection];
      vi.spyOn(productStoreService, 'addProductStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(productStoreService.query).toHaveBeenCalled();
      expect(productStoreService.addProductStoreToCollectionIfMissing).toHaveBeenCalledWith(
        productStoreCollection,
        ...additionalProductStores.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productStoresSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const department: IDepartment = { id: 15970 };
      const parent: IDepartment = { id: 29518 };
      department.parent = parent;
      const head: IParty = { id: 24527 };
      department.head = head;
      const productStore: IProductStore = { id: 30593 };
      department.productStore = productStore;

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection()).toContainEqual(parent);
      expect(comp.partiesSharedCollection()).toContainEqual(head);
      expect(comp.productStoresSharedCollection()).toContainEqual(productStore);
      expect(comp.department).toEqual(department);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDepartment>();
      const department = { id: 29518 };
      vi.spyOn(departmentFormService, 'getDepartment').mockReturnValue(department);
      vi.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(department);
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(expect.objectContaining(department));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDepartment>();
      const department = { id: 29518 };
      vi.spyOn(departmentFormService, 'getDepartment').mockReturnValue({ id: null });
      vi.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(department);
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(departmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDepartment>();
      const department = { id: 29518 };
      vi.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartment', () => {
      it('should forward to departmentService', () => {
        const entity = { id: 29518 };
        const entity2 = { id: 15970 };
        vi.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductStore', () => {
      it('should forward to productStoreService', () => {
        const entity = { id: 30593 };
        const entity2 = { id: 24662 };
        vi.spyOn(productStoreService, 'compareProductStore');
        comp.compareProductStore(entity, entity2);
        expect(productStoreService.compareProductStore).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
