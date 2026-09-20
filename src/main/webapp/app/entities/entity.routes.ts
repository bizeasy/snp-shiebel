import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'snpApp.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'status-category',
    title: 'snpApp.statusCategory.home.title',
    loadChildren: () => import('./status-category/status-category.routes'),
  },
  {
    path: 'status',
    title: 'snpApp.status.home.title',
    loadChildren: () => import('./status/status.routes'),
  },
  {
    path: 'status-valid-change',
    title: 'snpApp.statusValidChange.home.title',
    loadChildren: () => import('./status-valid-change/status-valid-change.routes'),
  },
  {
    path: 'system-parameters',
    title: 'snpApp.systemParameters.home.title',
    loadChildren: () => import('./system-parameters/system-parameters.routes'),
  },
  {
    path: 'content-type',
    title: 'snpApp.contentType.home.title',
    loadChildren: () => import('./content-type/content-type.routes'),
  },
  {
    path: 'attachment',
    title: 'snpApp.attachment.home.title',
    loadChildren: () => import('./attachment/attachment.routes'),
  },
  {
    path: 'file-folder',
    title: 'snpApp.fileFolder.home.title',
    loadChildren: () => import('./file-folder/file-folder.routes'),
  },
  {
    path: 'file-share',
    title: 'snpApp.fileShare.home.title',
    loadChildren: () => import('./file-share/file-share.routes'),
  },
  {
    path: 'geo-type',
    title: 'snpApp.geoType.home.title',
    loadChildren: () => import('./geo-type/geo-type.routes'),
  },
  {
    path: 'geo-assoc-type',
    title: 'snpApp.geoAssocType.home.title',
    loadChildren: () => import('./geo-assoc-type/geo-assoc-type.routes'),
  },
  {
    path: 'geo',
    title: 'snpApp.geo.home.title',
    loadChildren: () => import('./geo/geo.routes'),
  },
  {
    path: 'geo-assoc',
    title: 'snpApp.geoAssoc.home.title',
    loadChildren: () => import('./geo-assoc/geo-assoc.routes'),
  },
  {
    path: 'geo-point',
    title: 'snpApp.geoPoint.home.title',
    loadChildren: () => import('./geo-point/geo-point.routes'),
  },
  {
    path: 'party-type',
    title: 'snpApp.partyType.home.title',
    loadChildren: () => import('./party-type/party-type.routes'),
  },
  {
    path: 'party',
    title: 'snpApp.party.home.title',
    loadChildren: () => import('./party/party.routes'),
  },
  {
    path: 'role-type',
    title: 'snpApp.roleType.home.title',
    loadChildren: () => import('./role-type/role-type.routes'),
  },
  {
    path: 'party-role',
    title: 'snpApp.partyRole.home.title',
    loadChildren: () => import('./party-role/party-role.routes'),
  },
  {
    path: 'party-classification-type',
    title: 'snpApp.partyClassificationType.home.title',
    loadChildren: () => import('./party-classification-type/party-classification-type.routes'),
  },
  {
    path: 'party-classification-group',
    title: 'snpApp.partyClassificationGroup.home.title',
    loadChildren: () => import('./party-classification-group/party-classification-group.routes'),
  },
  {
    path: 'party-classification',
    title: 'snpApp.partyClassification.home.title',
    loadChildren: () => import('./party-classification/party-classification.routes'),
  },
  {
    path: 'party-identification-type',
    title: 'snpApp.partyIdentificationType.home.title',
    loadChildren: () => import('./party-identification-type/party-identification-type.routes'),
  },
  {
    path: 'party-identification',
    title: 'snpApp.partyIdentification.home.title',
    loadChildren: () => import('./party-identification/party-identification.routes'),
  },
  {
    path: 'party-relationship-type',
    title: 'snpApp.partyRelationshipType.home.title',
    loadChildren: () => import('./party-relationship-type/party-relationship-type.routes'),
  },
  {
    path: 'party-relationship',
    title: 'snpApp.partyRelationship.home.title',
    loadChildren: () => import('./party-relationship/party-relationship.routes'),
  },
  {
    path: 'contact-mech-purpose',
    title: 'snpApp.contactMechPurpose.home.title',
    loadChildren: () => import('./contact-mech-purpose/contact-mech-purpose.routes'),
  },
  {
    path: 'contact-mech-type',
    title: 'snpApp.contactMechType.home.title',
    loadChildren: () => import('./contact-mech-type/contact-mech-type.routes'),
  },
  {
    path: 'contact-mech',
    title: 'snpApp.contactMech.home.title',
    loadChildren: () => import('./contact-mech/contact-mech.routes'),
  },
  {
    path: 'party-contact-mech',
    title: 'snpApp.partyContactMech.home.title',
    loadChildren: () => import('./party-contact-mech/party-contact-mech.routes'),
  },
  {
    path: 'postal-address',
    title: 'snpApp.postalAddress.home.title',
    loadChildren: () => import('./postal-address/postal-address.routes'),
  },
  {
    path: 'otp-auth',
    title: 'snpApp.otpAuth.home.title',
    loadChildren: () => import('./otp-auth/otp-auth.routes'),
  },
  {
    path: 'product-store',
    title: 'snpApp.productStore.home.title',
    loadChildren: () => import('./product-store/product-store.routes'),
  },
  {
    path: 'department',
    title: 'snpApp.department.home.title',
    loadChildren: () => import('./department/department.routes'),
  },
  {
    path: 'department-member',
    title: 'snpApp.departmentMember.home.title',
    loadChildren: () => import('./department-member/department-member.routes'),
  },
  {
    path: 'uom-type',
    title: 'snpApp.uomType.home.title',
    loadChildren: () => import('./uom-type/uom-type.routes'),
  },
  {
    path: 'uom',
    title: 'snpApp.uom.home.title',
    loadChildren: () => import('./uom/uom.routes'),
  },
  {
    path: 'uom-conversion',
    title: 'snpApp.uomConversion.home.title',
    loadChildren: () => import('./uom-conversion/uom-conversion.routes'),
  },
  {
    path: 'catalogue',
    title: 'snpApp.catalogue.home.title',
    loadChildren: () => import('./catalogue/catalogue.routes'),
  },
  {
    path: 'catalogue-category-type',
    title: 'snpApp.catalogueCategoryType.home.title',
    loadChildren: () => import('./catalogue-category-type/catalogue-category-type.routes'),
  },
  {
    path: 'catalogue-category',
    title: 'snpApp.catalogueCategory.home.title',
    loadChildren: () => import('./catalogue-category/catalogue-category.routes'),
  },
  {
    path: 'product-category-type',
    title: 'snpApp.productCategoryType.home.title',
    loadChildren: () => import('./product-category-type/product-category-type.routes'),
  },
  {
    path: 'product-category',
    title: 'snpApp.productCategory.home.title',
    loadChildren: () => import('./product-category/product-category.routes'),
  },
  {
    path: 'product-category-member',
    title: 'snpApp.productCategoryMember.home.title',
    loadChildren: () => import('./product-category-member/product-category-member.routes'),
  },
  {
    path: 'product-type',
    title: 'snpApp.productType.home.title',
    loadChildren: () => import('./product-type/product-type.routes'),
  },
  {
    path: 'product',
    title: 'snpApp.product.home.title',
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'product-assoc-type',
    title: 'snpApp.productAssocType.home.title',
    loadChildren: () => import('./product-assoc-type/product-assoc-type.routes'),
  },
  {
    path: 'product-assoc',
    title: 'snpApp.productAssoc.home.title',
    loadChildren: () => import('./product-assoc/product-assoc.routes'),
  },
  {
    path: 'product-feature-category',
    title: 'snpApp.productFeatureCategory.home.title',
    loadChildren: () => import('./product-feature-category/product-feature-category.routes'),
  },
  {
    path: 'product-feature-category-appl',
    title: 'snpApp.productFeatureCategoryAppl.home.title',
    loadChildren: () => import('./product-feature-category-appl/product-feature-category-appl.routes'),
  },
  {
    path: 'product-feature-type',
    title: 'snpApp.productFeatureType.home.title',
    loadChildren: () => import('./product-feature-type/product-feature-type.routes'),
  },
  {
    path: 'product-feature-group',
    title: 'snpApp.productFeatureGroup.home.title',
    loadChildren: () => import('./product-feature-group/product-feature-group.routes'),
  },
  {
    path: 'product-feature-group-appl',
    title: 'snpApp.productFeatureGroupAppl.home.title',
    loadChildren: () => import('./product-feature-group-appl/product-feature-group-appl.routes'),
  },
  {
    path: 'product-feature',
    title: 'snpApp.productFeature.home.title',
    loadChildren: () => import('./product-feature/product-feature.routes'),
  },
  {
    path: 'product-feature-appl-type',
    title: 'snpApp.productFeatureApplType.home.title',
    loadChildren: () => import('./product-feature-appl-type/product-feature-appl-type.routes'),
  },
  {
    path: 'product-feature-appl',
    title: 'snpApp.productFeatureAppl.home.title',
    loadChildren: () => import('./product-feature-appl/product-feature-appl.routes'),
  },
  {
    path: 'product-price-type',
    title: 'snpApp.productPriceType.home.title',
    loadChildren: () => import('./product-price-type/product-price-type.routes'),
  },
  {
    path: 'product-price-purpose',
    title: 'snpApp.productPricePurpose.home.title',
    loadChildren: () => import('./product-price-purpose/product-price-purpose.routes'),
  },
  {
    path: 'product-price',
    title: 'snpApp.productPrice.home.title',
    loadChildren: () => import('./product-price/product-price.routes'),
  },
  {
    path: 'keyword-type',
    title: 'snpApp.keywordType.home.title',
    loadChildren: () => import('./keyword-type/keyword-type.routes'),
  },
  {
    path: 'product-keyword',
    title: 'snpApp.productKeyword.home.title',
    loadChildren: () => import('./product-keyword/product-keyword.routes'),
  },
  {
    path: 'supplier-product',
    title: 'snpApp.supplierProduct.home.title',
    loadChildren: () => import('./supplier-product/supplier-product.routes'),
  },
  {
    path: 'product-review',
    title: 'snpApp.productReview.home.title',
    loadChildren: () => import('./product-review/product-review.routes'),
  },
  {
    path: 'product-role',
    title: 'snpApp.productRole.home.title',
    loadChildren: () => import('./product-role/product-role.routes'),
  },
  {
    path: 'product-attachment',
    title: 'snpApp.productAttachment.home.title',
    loadChildren: () => import('./product-attachment/product-attachment.routes'),
  },
  {
    path: 'facility-type',
    title: 'snpApp.facilityType.home.title',
    loadChildren: () => import('./facility-type/facility-type.routes'),
  },
  {
    path: 'facility-group-type',
    title: 'snpApp.facilityGroupType.home.title',
    loadChildren: () => import('./facility-group-type/facility-group-type.routes'),
  },
  {
    path: 'facility-group',
    title: 'snpApp.facilityGroup.home.title',
    loadChildren: () => import('./facility-group/facility-group.routes'),
  },
  {
    path: 'facility',
    title: 'snpApp.facility.home.title',
    loadChildren: () => import('./facility/facility.routes'),
  },
  {
    path: 'location-type',
    title: 'snpApp.locationType.home.title',
    loadChildren: () => import('./location-type/location-type.routes'),
  },
  {
    path: 'facility-location',
    title: 'snpApp.facilityLocation.home.title',
    loadChildren: () => import('./facility-location/facility-location.routes'),
  },
  {
    path: 'facility-party',
    title: 'snpApp.facilityParty.home.title',
    loadChildren: () => import('./facility-party/facility-party.routes'),
  },
  {
    path: 'facility-group-member',
    title: 'snpApp.facilityGroupMember.home.title',
    loadChildren: () => import('./facility-group-member/facility-group-member.routes'),
  },
  {
    path: 'facility-contact-mech',
    title: 'snpApp.facilityContactMech.home.title',
    loadChildren: () => import('./facility-contact-mech/facility-contact-mech.routes'),
  },
  {
    path: 'facility-carrier-shipment',
    title: 'snpApp.facilityCarrierShipment.home.title',
    loadChildren: () => import('./facility-carrier-shipment/facility-carrier-shipment.routes'),
  },
  {
    path: 'product-facility',
    title: 'snpApp.productFacility.home.title',
    loadChildren: () => import('./product-facility/product-facility.routes'),
  },
  {
    path: 'product-facility-user',
    title: 'snpApp.productFacilityUser.home.title',
    loadChildren: () => import('./product-facility-user/product-facility-user.routes'),
  },
  {
    path: 'product-facility-location',
    title: 'snpApp.productFacilityLocation.home.title',
    loadChildren: () => import('./product-facility-location/product-facility-location.routes'),
  },
  {
    path: 'inventory-item-type',
    title: 'snpApp.inventoryItemType.home.title',
    loadChildren: () => import('./inventory-item-type/inventory-item-type.routes'),
  },
  {
    path: 'lot',
    title: 'snpApp.lot.home.title',
    loadChildren: () => import('./lot/lot.routes'),
  },
  {
    path: 'inventory-item',
    title: 'snpApp.inventoryItem.home.title',
    loadChildren: () => import('./inventory-item/inventory-item.routes'),
  },
  {
    path: 'inventory-item-delegate',
    title: 'snpApp.inventoryItemDelegate.home.title',
    loadChildren: () => import('./inventory-item-delegate/inventory-item-delegate.routes'),
  },
  {
    path: 'inventory-item-variance',
    title: 'snpApp.inventoryItemVariance.home.title',
    loadChildren: () => import('./inventory-item-variance/inventory-item-variance.routes'),
  },
  {
    path: 'inventory-transfer',
    title: 'snpApp.inventoryTransfer.home.title',
    loadChildren: () => import('./inventory-transfer/inventory-transfer.routes'),
  },
  {
    path: 'item-issuance',
    title: 'snpApp.itemIssuance.home.title',
    loadChildren: () => import('./item-issuance/item-issuance.routes'),
  },
  {
    path: 'shopping-list-type',
    title: 'snpApp.shoppingListType.home.title',
    loadChildren: () => import('./shopping-list-type/shopping-list-type.routes'),
  },
  {
    path: 'shopping-list',
    title: 'snpApp.shoppingList.home.title',
    loadChildren: () => import('./shopping-list/shopping-list.routes'),
  },
  {
    path: 'shopping-list-item',
    title: 'snpApp.shoppingListItem.home.title',
    loadChildren: () => import('./shopping-list-item/shopping-list-item.routes'),
  },
  {
    path: 'cust-request-type',
    title: 'snpApp.custRequestType.home.title',
    loadChildren: () => import('./cust-request-type/cust-request-type.routes'),
  },
  {
    path: 'cust-request-category',
    title: 'snpApp.custRequestCategory.home.title',
    loadChildren: () => import('./cust-request-category/cust-request-category.routes'),
  },
  {
    path: 'cust-request',
    title: 'snpApp.custRequest.home.title',
    loadChildren: () => import('./cust-request/cust-request.routes'),
  },
  {
    path: 'cust-request-item',
    title: 'snpApp.custRequestItem.home.title',
    loadChildren: () => import('./cust-request-item/cust-request-item.routes'),
  },
  {
    path: 'cust-request-role',
    title: 'snpApp.custRequestRole.home.title',
    loadChildren: () => import('./cust-request-role/cust-request-role.routes'),
  },
  {
    path: 'cust-request-attachment',
    title: 'snpApp.custRequestAttachment.home.title',
    loadChildren: () => import('./cust-request-attachment/cust-request-attachment.routes'),
  },
  {
    path: 'cust-request-item-attachment',
    title: 'snpApp.custRequestItemAttachment.home.title',
    loadChildren: () => import('./cust-request-item-attachment/cust-request-item-attachment.routes'),
  },
  {
    path: 'quote-type',
    title: 'snpApp.quoteType.home.title',
    loadChildren: () => import('./quote-type/quote-type.routes'),
  },
  {
    path: 'quote',
    title: 'snpApp.quote.home.title',
    loadChildren: () => import('./quote/quote.routes'),
  },
  {
    path: 'quote-item',
    title: 'snpApp.quoteItem.home.title',
    loadChildren: () => import('./quote-item/quote-item.routes'),
  },
  {
    path: 'quote-role',
    title: 'snpApp.quoteRole.home.title',
    loadChildren: () => import('./quote-role/quote-role.routes'),
  },
  {
    path: 'quote-attachment',
    title: 'snpApp.quoteAttachment.home.title',
    loadChildren: () => import('./quote-attachment/quote-attachment.routes'),
  },
  {
    path: 'order-type',
    title: 'snpApp.orderType.home.title',
    loadChildren: () => import('./order-type/order-type.routes'),
  },
  {
    path: 'sales-channel',
    title: 'snpApp.salesChannel.home.title',
    loadChildren: () => import('./sales-channel/sales-channel.routes'),
  },
  {
    path: 'custom-time-period',
    title: 'snpApp.customTimePeriod.home.title',
    loadChildren: () => import('./custom-time-period/custom-time-period.routes'),
  },
  {
    path: 'order',
    title: 'snpApp.order.home.title',
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'order-item-type',
    title: 'snpApp.orderItemType.home.title',
    loadChildren: () => import('./order-item-type/order-item-type.routes'),
  },
  {
    path: 'order-item',
    title: 'snpApp.orderItem.home.title',
    loadChildren: () => import('./order-item/order-item.routes'),
  },
  {
    path: 'order-status',
    title: 'snpApp.orderStatus.home.title',
    loadChildren: () => import('./order-status/order-status.routes'),
  },
  {
    path: 'order-contact-mech',
    title: 'snpApp.orderContactMech.home.title',
    loadChildren: () => import('./order-contact-mech/order-contact-mech.routes'),
  },
  {
    path: 'order-role',
    title: 'snpApp.orderRole.home.title',
    loadChildren: () => import('./order-role/order-role.routes'),
  },
  {
    path: 'order-attachment',
    title: 'snpApp.orderAttachment.home.title',
    loadChildren: () => import('./order-attachment/order-attachment.routes'),
  },
  {
    path: 'order-item-attachment',
    title: 'snpApp.orderItemAttachment.home.title',
    loadChildren: () => import('./order-item-attachment/order-item-attachment.routes'),
  },
  {
    path: 'order-adjustment-type',
    title: 'snpApp.orderAdjustmentType.home.title',
    loadChildren: () => import('./order-adjustment-type/order-adjustment-type.routes'),
  },
  {
    path: 'order-adjustment',
    title: 'snpApp.orderAdjustment.home.title',
    loadChildren: () => import('./order-adjustment/order-adjustment.routes'),
  },
  {
    path: 'quote-adjustment',
    title: 'snpApp.quoteAdjustment.home.title',
    loadChildren: () => import('./quote-adjustment/quote-adjustment.routes'),
  },
  {
    path: 'invoice-type',
    title: 'snpApp.invoiceType.home.title',
    loadChildren: () => import('./invoice-type/invoice-type.routes'),
  },
  {
    path: 'invoice',
    title: 'snpApp.invoice.home.title',
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'invoice-item-type',
    title: 'snpApp.invoiceItemType.home.title',
    loadChildren: () => import('./invoice-item-type/invoice-item-type.routes'),
  },
  {
    path: 'invoice-item',
    title: 'snpApp.invoiceItem.home.title',
    loadChildren: () => import('./invoice-item/invoice-item.routes'),
  },
  {
    path: 'invoice-item-type-map',
    title: 'snpApp.invoiceItemTypeMap.home.title',
    loadChildren: () => import('./invoice-item-type-map/invoice-item-type-map.routes'),
  },
  {
    path: 'invoice-role',
    title: 'snpApp.invoiceRole.home.title',
    loadChildren: () => import('./invoice-role/invoice-role.routes'),
  },
  {
    path: 'order-item-billing',
    title: 'snpApp.orderItemBilling.home.title',
    loadChildren: () => import('./order-item-billing/order-item-billing.routes'),
  },
  {
    path: 'payment-type',
    title: 'snpApp.paymentType.home.title',
    loadChildren: () => import('./payment-type/payment-type.routes'),
  },
  {
    path: 'payment-method-type',
    title: 'snpApp.paymentMethodType.home.title',
    loadChildren: () => import('./payment-method-type/payment-method-type.routes'),
  },
  {
    path: 'payment-method',
    title: 'snpApp.paymentMethod.home.title',
    loadChildren: () => import('./payment-method/payment-method.routes'),
  },
  {
    path: 'payment',
    title: 'snpApp.payment.home.title',
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'payment-application',
    title: 'snpApp.paymentApplication.home.title',
    loadChildren: () => import('./payment-application/payment-application.routes'),
  },
  {
    path: 'payment-gateway-config',
    title: 'snpApp.paymentGatewayConfig.home.title',
    loadChildren: () => import('./payment-gateway-config/payment-gateway-config.routes'),
  },
  {
    path: 'payment-gateway-response',
    title: 'snpApp.paymentGatewayResponse.home.title',
    loadChildren: () => import('./payment-gateway-response/payment-gateway-response.routes'),
  },
  {
    path: 'tax-authority',
    title: 'snpApp.taxAuthority.home.title',
    loadChildren: () => import('./tax-authority/tax-authority.routes'),
  },
  {
    path: 'tax-authority-rate-type',
    title: 'snpApp.taxAuthorityRateType.home.title',
    loadChildren: () => import('./tax-authority-rate-type/tax-authority-rate-type.routes'),
  },
  {
    path: 'tax-slab',
    title: 'snpApp.taxSlab.home.title',
    loadChildren: () => import('./tax-slab/tax-slab.routes'),
  },
  {
    path: 'shipment-type',
    title: 'snpApp.shipmentType.home.title',
    loadChildren: () => import('./shipment-type/shipment-type.routes'),
  },
  {
    path: 'shipment-method-type',
    title: 'snpApp.shipmentMethodType.home.title',
    loadChildren: () => import('./shipment-method-type/shipment-method-type.routes'),
  },
  {
    path: 'carrier-shipment-method',
    title: 'snpApp.carrierShipmentMethod.home.title',
    loadChildren: () => import('./carrier-shipment-method/carrier-shipment-method.routes'),
  },
  {
    path: 'shipment',
    title: 'snpApp.shipment.home.title',
    loadChildren: () => import('./shipment/shipment.routes'),
  },
  {
    path: 'shipment-item',
    title: 'snpApp.shipmentItem.home.title',
    loadChildren: () => import('./shipment-item/shipment-item.routes'),
  },
  {
    path: 'shipment-status',
    title: 'snpApp.shipmentStatus.home.title',
    loadChildren: () => import('./shipment-status/shipment-status.routes'),
  },
  {
    path: 'shipment-receipt',
    title: 'snpApp.shipmentReceipt.home.title',
    loadChildren: () => import('./shipment-receipt/shipment-receipt.routes'),
  },
  {
    path: 'shipment-attachment',
    title: 'snpApp.shipmentAttachment.home.title',
    loadChildren: () => import('./shipment-attachment/shipment-attachment.routes'),
  },
  {
    path: 'order-shipment',
    title: 'snpApp.orderShipment.home.title',
    loadChildren: () => import('./order-shipment/order-shipment.routes'),
  },
  {
    path: 'pick-list',
    title: 'snpApp.pickList.home.title',
    loadChildren: () => import('./pick-list/pick-list.routes'),
  },
  {
    path: 'pick-list-item',
    title: 'snpApp.pickListItem.home.title',
    loadChildren: () => import('./pick-list-item/pick-list-item.routes'),
  },
  {
    path: 'picklist-role',
    title: 'snpApp.picklistRole.home.title',
    loadChildren: () => import('./picklist-role/picklist-role.routes'),
  },
  {
    path: 'return-header-type',
    title: 'snpApp.returnHeaderType.home.title',
    loadChildren: () => import('./return-header-type/return-header-type.routes'),
  },
  {
    path: 'return-header',
    title: 'snpApp.returnHeader.home.title',
    loadChildren: () => import('./return-header/return-header.routes'),
  },
  {
    path: 'return-item',
    title: 'snpApp.returnItem.home.title',
    loadChildren: () => import('./return-item/return-item.routes'),
  },
  {
    path: 'return-item-billing',
    title: 'snpApp.returnItemBilling.home.title',
    loadChildren: () => import('./return-item-billing/return-item-billing.routes'),
  },
  {
    path: 'transaction-category',
    title: 'snpApp.transactionCategory.home.title',
    loadChildren: () => import('./transaction-category/transaction-category.routes'),
  },
  {
    path: 'transaction-type',
    title: 'snpApp.transactionType.home.title',
    loadChildren: () => import('./transaction-type/transaction-type.routes'),
  },
  {
    path: 'cash-book',
    title: 'snpApp.cashBook.home.title',
    loadChildren: () => import('./cash-book/cash-book.routes'),
  },
  {
    path: 'cash-book-attachment',
    title: 'snpApp.cashBookAttachment.home.title',
    loadChildren: () => import('./cash-book-attachment/cash-book-attachment.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
