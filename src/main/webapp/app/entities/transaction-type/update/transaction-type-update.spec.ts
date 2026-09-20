import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TransactionCategoryService } from 'app/entities/transaction-category/service/transaction-category.service';
import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

import { TransactionTypeFormService } from './transaction-type-form.service';
import { TransactionTypeUpdate } from './transaction-type-update';

describe('TransactionType Management Update Component', () => {
  let comp: TransactionTypeUpdate;
  let fixture: ComponentFixture<TransactionTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let transactionTypeFormService: TransactionTypeFormService;
  let transactionTypeService: TransactionTypeService;
  let transactionCategoryService: TransactionCategoryService;

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

    fixture = TestBed.createComponent(TransactionTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionTypeFormService = TestBed.inject(TransactionTypeFormService);
    transactionTypeService = TestBed.inject(TransactionTypeService);
    transactionCategoryService = TestBed.inject(TransactionCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TransactionCategory query and add missing value', () => {
      const transactionType: ITransactionType = { id: 32159 };
      const category: ITransactionCategory = { id: 23111 };
      transactionType.category = category;

      const transactionCategoryCollection: ITransactionCategory[] = [{ id: 23111 }];
      vi.spyOn(transactionCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionCategoryCollection })));
      const additionalTransactionCategories = [category];
      const expectedCollection: ITransactionCategory[] = [...additionalTransactionCategories, ...transactionCategoryCollection];
      vi.spyOn(transactionCategoryService, 'addTransactionCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      expect(transactionCategoryService.query).toHaveBeenCalled();
      expect(transactionCategoryService.addTransactionCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        transactionCategoryCollection,
        ...additionalTransactionCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transactionCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const transactionType: ITransactionType = { id: 32159 };
      const category: ITransactionCategory = { id: 23111 };
      transactionType.category = category;

      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      expect(comp.transactionCategoriesSharedCollection()).toContainEqual(category);
      expect(comp.transactionType).toEqual(transactionType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vi.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue(transactionType);
      vi.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionType);
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(transactionType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vi.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue({ id: null });
      vi.spyOn(transactionTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionType);
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(transactionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vi.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransactionCategory', () => {
      it('should forward to transactionCategoryService', () => {
        const entity = { id: 23111 };
        const entity2 = { id: 5448 };
        vi.spyOn(transactionCategoryService, 'compareTransactionCategory');
        comp.compareTransactionCategory(entity, entity2);
        expect(transactionCategoryService.compareTransactionCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
