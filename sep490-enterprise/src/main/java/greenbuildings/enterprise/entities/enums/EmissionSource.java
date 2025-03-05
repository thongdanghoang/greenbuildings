package greenbuildings.enterprise.entities.enums;

public enum EmissionSource
{
    EnergyIndustry, // Công nghiệp năng lượng
    ManufacturingAndConstruction, // Công nghiệp sản xuất và xây dựng
    DomesticAviation, // Giao thông vận tải hàng không nội địa
    RoadTransport, // Giao thông vận tải đường bộ
    InlandWaterwayAndDomesticShipping, // Giao thông vận tải đường thủy nội địa và hàng hải nội địa
    CommercialAndServices, // Thương mại và dịch vụ
    Residential, // Dân dụng
    AgricultureForestryFisheries, // Nông nghiệp, lâm nghiệp và thủy sản
    UndergroundCoalMining, // Khai thác than hầm lò
    SurfaceCoalMining, // Khai thác than lộ thiên
    OilExtraction, // Khai thác dầu
    NaturalGasExtraction, // Khai thác khí tự nhiên
    
    SolidWasteLandfills, // Phát thải từ bãi chôn lấp chất thải rắn
    IncinerationOfMunicipalSolidWaste, // Thiêu đốt chất thải (chất thải rắn sinh hoạt)
    BiologicalWasteTreatment, // Xử lý chất thải rắn bằng phương pháp sinh học
    WasteIncineration, // Thiêu đốt chất thải
    IncinerationMovingGrateContinuous, // Thiêu đốt chất thải - Lò đốt vỉ động liên tục
    IncinerationFluidizedBedContinuous, // Thiêu đốt chất thải - Lò đốt tầng sôi liên tục
    IncinerationMovingGrateSemiContinuous, // Thiêu đốt chất thải - Lò đốt vỉ động bán liên tục
    IncinerationFluidizedBedSemiContinuous, // Thiêu đốt chất thải - Lò đốt tầng sôi bán liên tục
    IncinerationMovingGrateBatch, // Thiêu đốt chất thải - Lò đốt vỉ động - đốt hàng loạt
    IncinerationFluidizedBedBatch, // Thiêu đốt chất thải - Lò đốt tầng sôi - đốt hàng loạt
    OpenBurningOfWaste, // Đốt lộ thiên chất thải
    DomesticWastewaterTreatmentAndDischarge, // Xử lý và xả nước thải sinh hoạt
    IndustrialWastewaterTreatmentAndDischarge, // Xử lý và xả nước thải công nghiệp
    
    ELECTRIC_CONSUMPTION
}
