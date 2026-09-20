import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductAttachment } from '../product-attachment.model';
import { ProductAttachmentService } from '../service/product-attachment.service';

import { ProductAttachmentFormService } from './product-attachment-form.service';
import { ProductAttachmentUpdate } from './product-attachment-update';

describe('ProductAttachment Management Update Component', () => {
  let comp: ProductAttachmentUpdate;
  let fixture: ComponentFixture<ProductAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let productAttachmentFormService: ProductAttachmentFormService;
  let productAttachmentService: ProductAttachmentService;
  let productService: ProductService;
  let attachmentService: AttachmentService;

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

    fixture = TestBed.createComponent(ProductAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productAttachmentFormService = TestBed.inject(ProductAttachmentFormService);
    productAttachmentService = TestBed.inject(ProductAttachmentService);
    productService = TestBed.inject(ProductService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productAttachment: IProductAttachment = { id: 6267 };
      const product: IProduct = { id: 21536 };
      productAttachment.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productAttachment });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const productAttachment: IProductAttachment = { id: 6267 };
      const attachment: IAttachment = { id: 8078 };
      productAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productAttachment: IProductAttachment = { id: 6267 };
      const product: IProduct = { id: 21536 };
      productAttachment.product = product;
      const attachment: IAttachment = { id: 8078 };
      productAttachment.attachment = attachment;

      activatedRoute.data = of({ productAttachment });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.productAttachment).toEqual(productAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAttachment>();
      const productAttachment = { id: 8437 };
      vi.spyOn(productAttachmentFormService, 'getProductAttachment').mockReturnValue(productAttachment);
      vi.spyOn(productAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAttachment);
      saveSubject.complete();

      // THEN
      expect(productAttachmentFormService.getProductAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(productAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAttachment>();
      const productAttachment = { id: 8437 };
      vi.spyOn(productAttachmentFormService, 'getProductAttachment').mockReturnValue({ id: null });
      vi.spyOn(productAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAttachment);
      saveSubject.complete();

      // THEN
      expect(productAttachmentFormService.getProductAttachment).toHaveBeenCalled();
      expect(productAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAttachment>();
      const productAttachment = { id: 8437 };
      vi.spyOn(productAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAttachment', () => {
      it('should forward to attachmentService', () => {
        const entity = { id: 8078 };
        const entity2 = { id: 16801 };
        vi.spyOn(attachmentService, 'compareAttachment');
        comp.compareAttachment(entity, entity2);
        expect(attachmentService.compareAttachment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
