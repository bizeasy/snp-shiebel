package com.snp.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration<Object, Object> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.snp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.snp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.snp.domain.User.class.getName());
            createCache(cm, com.snp.domain.Authority.class.getName());
            createCache(cm, com.snp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.snp.domain.StatusCategory.class.getName());
            createCache(cm, com.snp.domain.Status.class.getName());
            createCache(cm, com.snp.domain.StatusValidChange.class.getName());
            createCache(cm, com.snp.domain.SystemParameters.class.getName());
            createCache(cm, com.snp.domain.ContentType.class.getName());
            createCache(cm, com.snp.domain.Attachment.class.getName());
            createCache(cm, com.snp.domain.FileFolder.class.getName());
            createCache(cm, com.snp.domain.FileShare.class.getName());
            createCache(cm, com.snp.domain.GeoType.class.getName());
            createCache(cm, com.snp.domain.GeoAssocType.class.getName());
            createCache(cm, com.snp.domain.Geo.class.getName());
            createCache(cm, com.snp.domain.GeoAssoc.class.getName());
            createCache(cm, com.snp.domain.GeoPoint.class.getName());
            createCache(cm, com.snp.domain.PartyType.class.getName());
            createCache(cm, com.snp.domain.Party.class.getName());
            createCache(cm, com.snp.domain.RoleType.class.getName());
            createCache(cm, com.snp.domain.PartyRole.class.getName());
            createCache(cm, com.snp.domain.PartyClassificationType.class.getName());
            createCache(cm, com.snp.domain.PartyClassificationGroup.class.getName());
            createCache(cm, com.snp.domain.PartyClassification.class.getName());
            createCache(cm, com.snp.domain.PartyIdentificationType.class.getName());
            createCache(cm, com.snp.domain.PartyIdentification.class.getName());
            createCache(cm, com.snp.domain.PartyRelationshipType.class.getName());
            createCache(cm, com.snp.domain.PartyRelationship.class.getName());
            createCache(cm, com.snp.domain.ContactMechPurpose.class.getName());
            createCache(cm, com.snp.domain.ContactMechType.class.getName());
            createCache(cm, com.snp.domain.ContactMech.class.getName());
            createCache(cm, com.snp.domain.PartyContactMech.class.getName());
            createCache(cm, com.snp.domain.PostalAddress.class.getName());
            createCache(cm, com.snp.domain.OtpAuth.class.getName());
            createCache(cm, com.snp.domain.ProductStore.class.getName());
            createCache(cm, com.snp.domain.Department.class.getName());
            createCache(cm, com.snp.domain.DepartmentMember.class.getName());
            createCache(cm, com.snp.domain.UomType.class.getName());
            createCache(cm, com.snp.domain.Uom.class.getName());
            createCache(cm, com.snp.domain.UomConversion.class.getName());
            createCache(cm, com.snp.domain.Catalogue.class.getName());
            createCache(cm, com.snp.domain.CatalogueCategoryType.class.getName());
            createCache(cm, com.snp.domain.CatalogueCategory.class.getName());
            createCache(cm, com.snp.domain.ProductCategoryType.class.getName());
            createCache(cm, com.snp.domain.ProductCategory.class.getName());
            createCache(cm, com.snp.domain.ProductCategoryMember.class.getName());
            createCache(cm, com.snp.domain.ProductType.class.getName());
            createCache(cm, com.snp.domain.Product.class.getName());
            createCache(cm, com.snp.domain.ProductAssocType.class.getName());
            createCache(cm, com.snp.domain.ProductAssoc.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureCategory.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureCategoryAppl.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureType.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureGroup.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureGroupAppl.class.getName());
            createCache(cm, com.snp.domain.ProductFeature.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureApplType.class.getName());
            createCache(cm, com.snp.domain.ProductFeatureAppl.class.getName());
            createCache(cm, com.snp.domain.ProductPriceType.class.getName());
            createCache(cm, com.snp.domain.ProductPricePurpose.class.getName());
            createCache(cm, com.snp.domain.ProductPrice.class.getName());
            createCache(cm, com.snp.domain.KeywordType.class.getName());
            createCache(cm, com.snp.domain.ProductKeyword.class.getName());
            createCache(cm, com.snp.domain.SupplierProduct.class.getName());
            createCache(cm, com.snp.domain.ProductReview.class.getName());
            createCache(cm, com.snp.domain.ProductRole.class.getName());
            createCache(cm, com.snp.domain.ProductAttachment.class.getName());
            createCache(cm, com.snp.domain.FacilityType.class.getName());
            createCache(cm, com.snp.domain.FacilityGroupType.class.getName());
            createCache(cm, com.snp.domain.FacilityGroup.class.getName());
            createCache(cm, com.snp.domain.Facility.class.getName());
            createCache(cm, com.snp.domain.LocationType.class.getName());
            createCache(cm, com.snp.domain.FacilityLocation.class.getName());
            createCache(cm, com.snp.domain.FacilityParty.class.getName());
            createCache(cm, com.snp.domain.FacilityGroupMember.class.getName());
            createCache(cm, com.snp.domain.FacilityContactMech.class.getName());
            createCache(cm, com.snp.domain.FacilityCarrierShipment.class.getName());
            createCache(cm, com.snp.domain.ProductFacility.class.getName());
            createCache(cm, com.snp.domain.ProductFacilityUser.class.getName());
            createCache(cm, com.snp.domain.ProductFacilityLocation.class.getName());
            createCache(cm, com.snp.domain.InventoryItemType.class.getName());
            createCache(cm, com.snp.domain.Lot.class.getName());
            createCache(cm, com.snp.domain.InventoryItem.class.getName());
            createCache(cm, com.snp.domain.InventoryItemDelegate.class.getName());
            createCache(cm, com.snp.domain.InventoryItemVariance.class.getName());
            createCache(cm, com.snp.domain.InventoryTransfer.class.getName());
            createCache(cm, com.snp.domain.ItemIssuance.class.getName());
            createCache(cm, com.snp.domain.ShoppingListType.class.getName());
            createCache(cm, com.snp.domain.ShoppingList.class.getName());
            createCache(cm, com.snp.domain.ShoppingListItem.class.getName());
            createCache(cm, com.snp.domain.CustRequestType.class.getName());
            createCache(cm, com.snp.domain.CustRequestCategory.class.getName());
            createCache(cm, com.snp.domain.CustRequest.class.getName());
            createCache(cm, com.snp.domain.CustRequestItem.class.getName());
            createCache(cm, com.snp.domain.CustRequestRole.class.getName());
            createCache(cm, com.snp.domain.CustRequestAttachment.class.getName());
            createCache(cm, com.snp.domain.CustRequestItemAttachment.class.getName());
            createCache(cm, com.snp.domain.QuoteType.class.getName());
            createCache(cm, com.snp.domain.Quote.class.getName());
            createCache(cm, com.snp.domain.QuoteItem.class.getName());
            createCache(cm, com.snp.domain.QuoteRole.class.getName());
            createCache(cm, com.snp.domain.QuoteAttachment.class.getName());
            createCache(cm, com.snp.domain.OrderType.class.getName());
            createCache(cm, com.snp.domain.SalesChannel.class.getName());
            createCache(cm, com.snp.domain.CustomTimePeriod.class.getName());
            createCache(cm, com.snp.domain.Order.class.getName());
            createCache(cm, com.snp.domain.OrderItemType.class.getName());
            createCache(cm, com.snp.domain.OrderItem.class.getName());
            createCache(cm, com.snp.domain.OrderStatus.class.getName());
            createCache(cm, com.snp.domain.OrderContactMech.class.getName());
            createCache(cm, com.snp.domain.OrderRole.class.getName());
            createCache(cm, com.snp.domain.OrderAttachment.class.getName());
            createCache(cm, com.snp.domain.OrderItemAttachment.class.getName());
            createCache(cm, com.snp.domain.OrderAdjustmentType.class.getName());
            createCache(cm, com.snp.domain.OrderAdjustment.class.getName());
            createCache(cm, com.snp.domain.QuoteAdjustment.class.getName());
            createCache(cm, com.snp.domain.InvoiceType.class.getName());
            createCache(cm, com.snp.domain.Invoice.class.getName());
            createCache(cm, com.snp.domain.InvoiceItemType.class.getName());
            createCache(cm, com.snp.domain.InvoiceItem.class.getName());
            createCache(cm, com.snp.domain.InvoiceItemTypeMap.class.getName());
            createCache(cm, com.snp.domain.InvoiceRole.class.getName());
            createCache(cm, com.snp.domain.OrderItemBilling.class.getName());
            createCache(cm, com.snp.domain.PaymentType.class.getName());
            createCache(cm, com.snp.domain.PaymentMethodType.class.getName());
            createCache(cm, com.snp.domain.PaymentMethod.class.getName());
            createCache(cm, com.snp.domain.Payment.class.getName());
            createCache(cm, com.snp.domain.PaymentApplication.class.getName());
            createCache(cm, com.snp.domain.PaymentGatewayConfig.class.getName());
            createCache(cm, com.snp.domain.PaymentGatewayResponse.class.getName());
            createCache(cm, com.snp.domain.TaxAuthority.class.getName());
            createCache(cm, com.snp.domain.TaxAuthorityRateType.class.getName());
            createCache(cm, com.snp.domain.TaxSlab.class.getName());
            createCache(cm, com.snp.domain.ShipmentType.class.getName());
            createCache(cm, com.snp.domain.ShipmentMethodType.class.getName());
            createCache(cm, com.snp.domain.CarrierShipmentMethod.class.getName());
            createCache(cm, com.snp.domain.Shipment.class.getName());
            createCache(cm, com.snp.domain.ShipmentItem.class.getName());
            createCache(cm, com.snp.domain.ShipmentStatus.class.getName());
            createCache(cm, com.snp.domain.ShipmentReceipt.class.getName());
            createCache(cm, com.snp.domain.ShipmentAttachment.class.getName());
            createCache(cm, com.snp.domain.OrderShipment.class.getName());
            createCache(cm, com.snp.domain.PickList.class.getName());
            createCache(cm, com.snp.domain.PickListItem.class.getName());
            createCache(cm, com.snp.domain.PicklistRole.class.getName());
            createCache(cm, com.snp.domain.ReturnHeaderType.class.getName());
            createCache(cm, com.snp.domain.ReturnHeader.class.getName());
            createCache(cm, com.snp.domain.ReturnItem.class.getName());
            createCache(cm, com.snp.domain.ReturnItemBilling.class.getName());
            createCache(cm, com.snp.domain.TransactionCategory.class.getName());
            createCache(cm, com.snp.domain.TransactionType.class.getName());
            createCache(cm, com.snp.domain.CashBook.class.getName());
            createCache(cm, com.snp.domain.CashBookAttachment.class.getName());
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
