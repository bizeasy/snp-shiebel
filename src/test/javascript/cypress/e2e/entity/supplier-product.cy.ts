import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('SupplierProduct e2e test', () => {
  const supplierProductPageUrl = '/supplier-product';
  let username: string;
  let password: string;
  const supplierProductSample = { fromDate: '2023-12-14T07:58:55.014Z' };

  let supplierProduct;
  let product;
  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {
        name: 'celsius',
        internalName: 'advocate meaty',
        brandName: 'mid',
        variant: 'swelter blacken boo',
        sku: 'till dissemble',
        introductionDate: '2023-12-14T04:08:23.234Z',
        releaseDate: '2023-12-14T07:32:57.916Z',
        quantityIncluded: 14621.63,
        piecesIncluded: 20770,
        weight: 13817.43,
        height: 21517.84,
        width: 29184.01,
        depth: 22257.28,
        smallImageUrl: 'helpless which suckle',
        mediumImageUrl: 'unnaturally',
        largeImageUrl: 'aha naturally tough',
        detailImageUrl: 'arcade',
        originalImageUrl: 'honored',
        altImageUrl1: 'toward',
        altImageUrl2: 'supposing',
        altImageUrl3: 'affectionate ravage',
        altImageUrl4: 'vibraphone',
        altImageUrl5: 'appliance eyeglasses dead',
        altImageUrl6: 'uh-huh atop circumference',
        videoUrl: 'for discontinue',
        quantity: 21913.27,
        price: 27985.75,
        cgst: 3274.96,
        igst: 29974.76,
        sgst: 32722.63,
        cgstPercentage: 28376.83,
        igstPercentage: 27347.35,
        sgstPercentage: 16759.11,
        totalPrice: 21212.38,
        description: 'where unless queasily',
        longDescription: 'zany',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: false,
        requireInventory: true,
        returnable: true,
        isPopular: false,
        isBanner: false,
        bannerUrl: 'anenst westernize',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'barring',
        organisationShortName: 'too wherever',
        salutation: 'anneal concerned aro',
        firstName: 'Braulio',
        middleName: 'fooey suffice dash',
        lastName: 'Lind-Marquardt',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'acidly until punctua',
        primaryEmail: 'absent across',
        isTemporaryPassword: false,
        logoImageUrl: 'accountability including unwieldy',
        profileImageUrl: 'after reconsideration',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/supplier-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/supplier-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/supplier-products/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (supplierProduct) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/supplier-products/${supplierProduct.id}`,
      }).then(() => {
        supplierProduct = undefined;
      });
    }
  });

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('SupplierProducts menu should load SupplierProducts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('supplier-product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SupplierProduct').should('exist');
    cy.location('pathname').should('eq', supplierProductPageUrl);
  });

  describe('SupplierProduct page', () => {
    it('should have translated page title', () => {
      cy.visit(supplierProductPageUrl);
      cy.getEntityHeading('SupplierProduct').should('not.contain', 'snpApp.supplierProduct.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(supplierProductPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SupplierProduct page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${supplierProductPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('SupplierProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', supplierProductPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/supplier-products',
          body: {
            ...supplierProductSample,
            product,
            party,
          },
        }).then(({ body }) => {
          supplierProduct = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/supplier-products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/supplier-products?page=0&size=20>; rel="last",<http://localhost/api/supplier-products?page=0&size=20>; rel="first"',
              },
              body: [supplierProduct],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(supplierProductPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SupplierProduct page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('supplierProduct');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', supplierProductPageUrl);
      });

      it('edit button click should load edit SupplierProduct page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupplierProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', supplierProductPageUrl);
      });

      it('edit button click should load edit SupplierProduct page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupplierProduct');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', supplierProductPageUrl);
      });

      it('last delete button click should delete instance of SupplierProduct', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('supplierProduct').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', supplierProductPageUrl);

        supplierProduct = undefined;
      });
    });
  });

  describe('new SupplierProduct page', () => {
    beforeEach(() => {
      cy.visit(supplierProductPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SupplierProduct');
    });

    it('should create an instance of SupplierProduct', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T05:20');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T05:20');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T17:18');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T17:18');

      cy.get(`[data-cy="supplierProductId"]`).type('prudent');
      cy.get(`[data-cy="supplierProductId"]`).should('have.value', 'prudent');

      cy.get(`[data-cy="minOrderQty"]`).type('2008.13');
      cy.get(`[data-cy="minOrderQty"]`).should('have.value', '2008.13');

      cy.get(`[data-cy="lastPrice"]`).type('23547.76');
      cy.get(`[data-cy="lastPrice"]`).should('have.value', '23547.76');

      cy.get(`[data-cy="shippingPrice"]`).type('20999.8');
      cy.get(`[data-cy="shippingPrice"]`).should('have.value', '20999.8');

      cy.get(`[data-cy="cgst"]`).type('7597.5');
      cy.get(`[data-cy="cgst"]`).should('have.value', '7597.5');

      cy.get(`[data-cy="igst"]`).type('16637.67');
      cy.get(`[data-cy="igst"]`).should('have.value', '16637.67');

      cy.get(`[data-cy="sgst"]`).type('26271');
      cy.get(`[data-cy="sgst"]`).should('have.value', '26271');

      cy.get(`[data-cy="totalPrice"]`).type('16235.25');
      cy.get(`[data-cy="totalPrice"]`).should('have.value', '16235.25');

      cy.get(`[data-cy="comments"]`).type('contractor actually suspiciously');
      cy.get(`[data-cy="comments"]`).should('have.value', 'contractor actually suspiciously');

      cy.get(`[data-cy="leadTimeDays"]`).type('25356');
      cy.get(`[data-cy="leadTimeDays"]`).should('have.value', '25356');

      cy.get(`[data-cy="unitsIncluded"]`).type('26228.61');
      cy.get(`[data-cy="unitsIncluded"]`).should('have.value', '26228.61');

      cy.get(`[data-cy="canDropShip"]`).should('not.be.checked');
      cy.get(`[data-cy="canDropShip"]`).click();
      cy.get(`[data-cy="canDropShip"]`).should('be.checked');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        supplierProduct = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', supplierProductPageUrl);
    });
  });
});
