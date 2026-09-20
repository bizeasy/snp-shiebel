import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TransactionCategoryService } from '../service/transaction-category.service';
import { ITransactionCategory } from '../transaction-category.model';

import { TransactionCategoryFormService } from './transaction-category-form.service';
import { TransactionCategoryUpdate } from './transaction-category-update';

describe('TransactionCategory Management Update Component', () => {
  let comp: TransactionCategoryUpdate;
  let fixture: ComponentFixture<TransactionCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let transactionCategoryFormService: TransactionCategoryFormService;
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

    fixture = TestBed.createComponent(TransactionCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionCategoryFormService = TestBed.inject(TransactionCategoryFormService);
    transactionCategoryService = TestBed.inject(TransactionCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transactionCategory: ITransactionCategory = { id: 5448 };

      activatedRoute.data = of({ transactionCategory });
      comp.ngOnInit();

      expect(comp.transactionCategory).toEqual(transactionCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionCategory>();
      const transactionCategory = { id: 23111 };
      vi.spyOn(transactionCategoryFormService, 'getTransactionCategory').mockReturnValue(transactionCategory);
      vi.spyOn(transactionCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionCategory);
      saveSubject.complete();

      // THEN
      expect(transactionCategoryFormService.getTransactionCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(transactionCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionCategory>();
      const transactionCategory = { id: 23111 };
      vi.spyOn(transactionCategoryFormService, 'getTransactionCategory').mockReturnValue({ id: null });
      vi.spyOn(transactionCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionCategory);
      saveSubject.complete();

      // THEN
      expect(transactionCategoryFormService.getTransactionCategory).toHaveBeenCalled();
      expect(transactionCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionCategory>();
      const transactionCategory = { id: 23111 };
      vi.spyOn(transactionCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
